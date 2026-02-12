package cz.ivosahlik.ai_ecommerce_support.websocket;

public interface WebSocketMessageSender {

    void sendMessageToUser(String sessionId, String message);

}
