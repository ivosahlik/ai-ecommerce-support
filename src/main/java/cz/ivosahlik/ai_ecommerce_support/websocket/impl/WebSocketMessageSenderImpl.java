package cz.ivosahlik.ai_ecommerce_support.websocket.impl;

import cz.ivosahlik.ai_ecommerce_support.websocket.WebSocketMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketMessageSenderImpl implements WebSocketMessageSender {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessageToUser(String sessionId, String message) {
        String destination = "/topic/message/" + sessionId;
        messagingTemplate.convertAndSend(destination, message);
    }

}
