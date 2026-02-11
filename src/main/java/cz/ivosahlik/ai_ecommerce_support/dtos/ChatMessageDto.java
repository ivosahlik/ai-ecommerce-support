package cz.ivosahlik.ai_ecommerce_support.dtos;

import lombok.Data;

@Data
public class ChatMessageDto {
    private String sessionId;
    private String message;

}
