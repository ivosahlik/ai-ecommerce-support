package cz.ivosahlik.ai_ecommerce_support.service.chat;

import cz.ivosahlik.ai_ecommerce_support.dtos.ChatMessageDto;

public interface IConversationService {
    String handleChatMessage(ChatMessageDto message);
}
