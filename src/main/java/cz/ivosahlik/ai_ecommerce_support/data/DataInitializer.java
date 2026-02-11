package cz.ivosahlik.ai_ecommerce_support.data;

import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import cz.ivosahlik.ai_ecommerce_support.repository.ConversationRepository;
import cz.ivosahlik.ai_ecommerce_support.repository.CustomerRepository;
import cz.ivosahlik.ai_ecommerce_support.repository.TicketRepository;
import cz.ivosahlik.ai_ecommerce_support.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ConversationRepository conversationRepository;
    private final TicketService ticketService;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    //@PostConstruct
    public void init() {
        generateSeedData();
    }

    private void generateSeedData() {
        List<Customer> customers = createCustomers();

        for (int i = 1; i <= 10; i++) {
            Conversation conversation = new Conversation();
            String title = "Conversation Title " + i;
            String summary = "Lorem Ipsum is simply dummy text of the printing and " +
                    "typesetting industry. Lorem Ipsum has been the industry's standard" +
                    " dummy text ever since the 1500s,the leap into electronic typesetting," +
                    " remaining essentially unchanged.";
            // Set conversation title and user
            conversation.setConversationTitle(title);
            conversation.setConversationSummary(summary);
            conversation.setTicketCreated(false);
            conversation.setCustomer(customers.get(i % customers.size())); // Assign a user to the conversation

            // Save conversation
            Conversation savedConversation = conversationRepository.save(conversation);
            Ticket ticket = ticketService.createTicketForConversation(savedConversation);
            Ticket savedTicket = ticketRepository.save(ticket);

            savedConversation.setTicketCreated(true);
            savedConversation.setTicket(savedTicket);
            conversationRepository.save(savedConversation);
        }
    }

    private List<Customer> createCustomers() {
        List<Customer> customers = IntStream.rangeClosed(1, 5)
                .mapToObj(this::createCustomer)
                .toList();

        return customerRepository.saveAll(customers);
    }

    private Customer createCustomer(int i) {
        Customer c = new Customer();
        c.setFullName("User " + i);
        c.setEmailAddress("email" + i + "@example.com");
        c.setPhoneNumber("123456789" + i);
        return c;
    }
}
