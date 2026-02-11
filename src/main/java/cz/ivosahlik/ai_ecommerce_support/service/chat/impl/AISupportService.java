package cz.ivosahlik.ai_ecommerce_support.service.chat.impl;
import cz.ivosahlik.ai_ecommerce_support.dtos.ChatEntry;
import cz.ivosahlik.ai_ecommerce_support.service.chat.IAISupportService;
import cz.ivosahlik.ai_ecommerce_support.utils.PromptTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AISupportService implements IAISupportService {

    private final ChatClient chatClient;

    @Override
    public Mono<String> chatWithHistory(List<ChatEntry> history) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(PromptTemplates.AI_SUPPORT_PROMPT));

        history.forEach(chatEntry -> {
            String role = chatEntry.getRole();
            String content = chatEntry.getContent();
            switch (role) {
                case "user" -> messages.add(new UserMessage(content));
                case "assistant" -> messages.add(new AssistantMessage(content));
                default -> {
                }
            }
        });
        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("No content available");
            }
            return content;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> generateUserConfirmationMessage() {
        // Inject the conversation summary into the prompt
        String prompt = String.format(PromptTemplates.CUSTOMER_CONFIRMATION_PROMPT);

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(prompt));

        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("AI response content is null");
            }
            return content;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> summarizeUserConversation(String userConversationText) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(PromptTemplates.CUSTOMER_CONVERSATION_SUMMARY_PROMPT));
        messages.add(new UserMessage(userConversationText));
        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("AI response content is null");
            }
            return content.trim();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> generateConversationTitle(String summarizedConversation) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(PromptTemplates.TITLE_GENERATION_PROMPT));
        messages.add(new SystemMessage(summarizedConversation));
        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("AI response content is null");
            }
            return content.trim();
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @Override
    public Mono<String> generateEmailNotificationMessage() {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(PromptTemplates.EMAIL_NOTIFICATION_PROMPT));
        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("AI response content is null");
            }
            return content;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<String>> generateResolutionSuggestions(String complaintSummary) {
        String prompt = String.format(PromptTemplates.RESOLUTION_SUGGESTIONS_PROMPT, complaintSummary);
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(prompt));

        return Mono.fromCallable(() -> {
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .messages(messages)
                    .call();
            String content = responseSpec.content();
            if (content == null) {
                throw new IllegalStateException("AI response content is null");
            }
            return parseSuggestionsFromResponse(content.trim());
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private List<String> parseSuggestionsFromResponse(String aiResponse) {
        return Arrays.stream(aiResponse.split("\n"))
                .filter(line -> line.matches("^\\d\\.\\s.*"))
                .map(line -> line.replaceFirst("^\\d\\.\\s", "").trim())
                .toList();
    }

}
