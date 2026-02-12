package cz.ivosahlik.ai_ecommerce_support.service.chat.impl;

import cz.ivosahlik.ai_ecommerce_support.dtos.ChatEntry;
import cz.ivosahlik.ai_ecommerce_support.dtos.ChatMessageDto;
import cz.ivosahlik.ai_ecommerce_support.event.TicketCreationEvent;
import cz.ivosahlik.ai_ecommerce_support.helper.CustomerInfo;
import cz.ivosahlik.ai_ecommerce_support.helper.CustomerInfoHelper;
import cz.ivosahlik.ai_ecommerce_support.helper.RegexPattern;
import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import cz.ivosahlik.ai_ecommerce_support.repository.ConversationRepository;
import cz.ivosahlik.ai_ecommerce_support.repository.TicketRepository;
import cz.ivosahlik.ai_ecommerce_support.repository.UserRepository;
import cz.ivosahlik.ai_ecommerce_support.service.chat.IConversationService;
import cz.ivosahlik.ai_ecommerce_support.service.ticket.ITicketService;
import cz.ivosahlik.ai_ecommerce_support.utils.MessageUtil;
import cz.ivosahlik.ai_ecommerce_support.websocket.WebSocketMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {
    private final AISupportService aiSupportService;
    private final UserRepository userRepository;
    private final WebSocketMessageSender webSocketMessageSender;
    private final ApplicationEventPublisher publisher;

    private final Map<String, List<ChatEntry>> activeConversations = new ConcurrentHashMap<>();

    //This will handle the case where the customer enters wrong contact information
    private final Map<String, Boolean> waitingForContactCorrection = new ConcurrentHashMap<>();

    private final ConversationRepository conversationRepository;
    private final ITicketService iTicketService;
    private final TicketRepository ticketRepository;
    private final Executor executor;

    @Override
    public String handleChatMessage(ChatMessageDto chatMessage) {
        String sessionId = chatMessage.getSessionId();
        String userMessage = chatMessage.getMessage() != null ? chatMessage.getMessage().trim() : "";

        List<ChatEntry> history = activeConversations.computeIfAbsent(sessionId,
                k -> Collections.synchronizedList(new ArrayList<>()));

        String correctedCustomerInformation = getCorrectedCustomerInformation(sessionId, userMessage, history);
        log.info("********************* The corrected customer information: {}", correctedCustomerInformation);
        if (correctedCustomerInformation != null) {
            return correctedCustomerInformation;
        }

        history.add(new ChatEntry("user", userMessage));

        String aiResponseText;
        try {
            aiResponseText = aiSupportService.chatWithHistory(history).block();
        } catch (Exception e) {
            aiResponseText = "Sorry, I'm having trouble processing response right now.";
        }

        if (aiResponseText == null || aiResponseText.isEmpty()) {
            return "";
        }

        history.add(new ChatEntry("assistant", aiResponseText));

        if (aiResponseText.contains("TICKET_CREATION_READY")) {
            try {
                String confirmationMessage = aiSupportService.generateUserConfirmationMessage().block();
                history.add(new ChatEntry("assistant", confirmationMessage));

                finalizeConversationAndNotify(sessionId, history);
                return confirmationMessage;
            } catch (Exception e) {
                return "Error generating confirmation message.";
            }
        }

        return aiResponseText;
    }


    private Ticket finalizeConversationAndCreateTicket(String sessionId) {
        List<ChatEntry> history = activeConversations.get(sessionId);
        Customer customer = getCustomerInformation(history);
        log.info("The initial customer information: {}", customer);
        if (customer == null) {
            String errorMessage = MessageUtil.INVALID_CONTACT_INFO_MESSAGE;
            //Here we are going to send the message to the customer. (Websocket)
            webSocketMessageSender.sendMessageToUser(sessionId, errorMessage);
            if (history != null) {
                history.add(new ChatEntry("system", errorMessage));
            }
            // set up flag and wait for customer information correction
            waitingForContactCorrection.put(sessionId, true);
            return null;
        }
        waitingForContactCorrection.remove(sessionId);
        // get the conversation
        Conversation conversation = getConversation(customer);
        try {
            List<ChatEntry> userConversation = history.stream()
                    .filter(entry -> "user".equals(entry.getRole()))
                    .toList();

            //Ask the AI to summarize the customer conversation
            String conversationSummary = aiSupportService.summarizeUserConversation(userConversation.toString()).block();
            // Ask the AI to generate a suitable title for the conversation
            String conversationTitle = aiSupportService.generateConversationTitle(conversationSummary).block();
            conversation.setConversationTitle(conversationTitle != null ? conversationTitle.trim() : "Untitled Conversation");
            conversation.setConversationSummary(conversationSummary);
            Conversation savedConversation = conversationRepository.save(conversation);
            //Create and save the ticket for the conversation
            // Get the ticket details from the ticket service class
            Ticket ticket = iTicketService.createTicketForConversation(conversation);

            // Extract the order number from the chat history
            CustomerInfo customerInfo = getCustomerInfo(history);
            if (customerInfo.orderNumber() != null) {
                ticket.setProductOrderNumber(customerInfo.orderNumber());
            } else {
                ticket.setProductOrderNumber(null);
            }
            Ticket savedTicket = ticketRepository.save(ticket);

            savedConversation.setTicket(savedTicket);
            savedConversation.setTicketCreated(true);
            conversationRepository.save(savedConversation);
            //Send notification email to the customer
            publisher.publishEvent(new TicketCreationEvent(savedTicket));
            //Remove the customer conversation from the memory
            activeConversations.remove(sessionId);
            return savedTicket;

        } catch (Exception e) {
            String errorMsg = "Error occurred during conversation creation." + e.getMessage();
            webSocketMessageSender.sendMessageToUser(sessionId, errorMsg);
            return null;
        }
    }

    private void finalizeConversationAndNotify(String sessionId, List<ChatEntry> history) {
        CompletableFuture.runAsync(() -> {
            try {
                Ticket tempTicket = finalizeConversationAndCreateTicket(sessionId);
                if (tempTicket != null) {
                    history.add(new ChatEntry("system", "The email notification has been sent."));

                    String feedbackMessage = aiSupportService.generateEmailNotificationMessage().block();
                    if (feedbackMessage != null) {
                        List<ChatEntry> currentHistory = activeConversations.get(sessionId);
                        if (currentHistory != null) {
                            currentHistory.add(new ChatEntry("assistant", feedbackMessage));
                        }
                        webSocketMessageSender.sendMessageToUser(sessionId, feedbackMessage);
                    }
                }

            } catch (Exception e) {
                log.error("Error in async ticket creation and notification", e);
            }
        }, executor).exceptionally(e -> {
            log.error("Error in async ticket creation and notification", e);
            return null;
        });
    }

/*    @Async("asyncExecutor")
    private void finalizeConversationAndNotify(String sessionId, List<ChatEntry> history) {
        try {
            Ticket tempTicket = finalizeConversationAndCreateTicket(sessionId);
            if (tempTicket != null) {
                history.add(new ChatEntry("system", "The email notification has been sent."));

                String feedbackMessage = aiSupportService.generateEmailNotificationMessage().block();
                if (feedbackMessage != null) {
                    List<ChatEntry> currentHistory = activeConversations.get(sessionId);
                    if (currentHistory != null) {
                        currentHistory.add(new ChatEntry("assistant", feedbackMessage));
                    }
                    webSocketMessageSender.sendMessageToUser(sessionId, feedbackMessage);
                }
            }

        } catch (Exception e) {
            log.error("Error in async ticket creation and notification", e);
        }
    }*/

    private static Conversation getConversation(Customer customer) {
        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setTicketCreated(false);
        return conversation;
    }

    private Customer getCustomerInformation(List<ChatEntry> history) {
        CustomerInfo customerInfo = getCustomerInfo(history);
        log.info("***************** Customer information from the getCustomer method: {}", customerInfo);
        return userRepository.findByEmailAddressAndPhoneNumber(
                customerInfo.emailAddress(), customerInfo.phoneNumber());
    }

    private static CustomerInfo getCustomerInfo(List<ChatEntry> history) {
        return CustomerInfoHelper.extractUserInformationFromChatHistory(history);
    }

    private String getCorrectedCustomerInformation(String sessionId, String userMessage, List<ChatEntry> history) {
        if (Boolean.TRUE.equals(waitingForContactCorrection.get(sessionId))) {
            CustomerInfo customerInfo = CustomerInfoHelper.extractCustomerInfoFromMostCurrentMessage(userMessage);

            log.info("The corrected customer information 1: {}", customerInfo);

            replaceOldContactInformationInHistory(history, customerInfo.emailAddress(), customerInfo.phoneNumber());
            CustomerInfo customer = CustomerInfoHelper.extractUserInformationFromChatHistory(history);

            log.info("The corrected customer information 2: {}", customer);

            Optional<Customer> theCustomer = Optional.ofNullable(
                    userRepository.findByEmailAddressAndPhoneNumber(customer.emailAddress(), customer.phoneNumber()));

            log.info("The corrected customer information 3: {}", theCustomer);

            if (theCustomer.isPresent()) {
                waitingForContactCorrection.remove(sessionId);
                //Finalize conversation and send notification
                finalizeConversationAndNotify(sessionId, history);
                return MessageUtil.CONTACT_UPDATE_SUCCESS_MESSAGE;
            } else {
                return MessageUtil.INVALID_CONTACT_INFO_MESSAGE;
            }
        }
        return null;
    }

    private void replaceOldContactInformationInHistory(List<ChatEntry> history, String email, String phone) {
        if (history == null || history.isEmpty()) return;
        correctCustomerContactInformation(history, email, RegexPattern.EMAIL_PATTERN);
        correctCustomerContactInformation(history, phone, RegexPattern.PHONE_PATTERN);
    }

  /*  private void correctCustomerContactInformation(List<ChatEntry> history, String newContact, Pattern pattern) {
        if (newContact == null || newContact.isEmpty()) return;

        OptionalInt indexOpt = IntStream.range(0, history.size())
                .filter(i -> "user".equalsIgnoreCase(history.get(i).getRole())
                        && pattern.matcher(history.get(i).getContent())
                        .find()).findFirst();
        log.info("The found index: {}", indexOpt.toString());
        indexOpt.ifPresentOrElse(idx -> history.set(idx, new ChatEntry(
                        "user", newContact)),
                () -> history.add(new ChatEntry("user", newContact)));
    }*/

    private void correctCustomerContactInformation(List<ChatEntry> history, String newContact, Pattern pattern) {
        if (newContact == null || newContact.isEmpty()) return;

        for (int i = 0; i < history.size(); i++) {
            ChatEntry entry = history.get(i);
            if ("user".equalsIgnoreCase(entry.getRole())) {
                String content = entry.getContent();
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    // Replace all occurrences of old contact info with newContact
                    String updatedContent = matcher.replaceAll(newContact);
                    history.set(i, new ChatEntry("user", updatedContent));
                    log.info("Replaced contact info in history at index {}: {}", i, updatedContent);
                }
            }
        }
    }
}
