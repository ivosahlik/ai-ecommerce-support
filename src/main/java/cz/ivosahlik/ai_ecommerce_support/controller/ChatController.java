package cz.ivosahlik.ai_ecommerce_support.controller;

import cz.ivosahlik.ai_ecommerce_support.dtos.ChatMessageDto;
import cz.ivosahlik.ai_ecommerce_support.service.chat.IConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final IConversationService conversationService;

    @PostMapping("${api.prefix}/chat")
    public ResponseEntity<String> handleChatMessage(@RequestBody ChatMessageDto message){
        String response = conversationService.handleChatMessage(message);
        return ResponseEntity.ok(response);
    }
}
