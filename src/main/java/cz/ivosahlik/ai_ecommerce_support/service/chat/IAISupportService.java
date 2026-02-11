package cz.ivosahlik.ai_ecommerce_support.service.chat;

import cz.ivosahlik.ai_ecommerce_support.dtos.ChatEntry;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAISupportService {
    Mono<String> chatWithHistory(List<ChatEntry> history);

    Mono<String> generateUserConfirmationMessage();

    Mono<String> summarizeUserConversation(String userConversationText);

    Mono<String> generateConversationTitle(String summarizedConversation);

    Mono<String> generateEmailNotificationMessage();

    Mono<List<String>> generateResolutionSuggestions(String complaintSummary);
}
