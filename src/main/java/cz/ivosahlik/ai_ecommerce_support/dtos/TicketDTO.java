package cz.ivosahlik.ai_ecommerce_support.dtos;

import cz.ivosahlik.ai_ecommerce_support.enums.TicketStatus;
import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private Long productOrderNumber;
    private String referenceNumber;
    private String resolutionDetails;
    private Conversation conversation;
}
