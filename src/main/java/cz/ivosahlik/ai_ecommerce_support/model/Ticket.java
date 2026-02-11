package cz.ivosahlik.ai_ecommerce_support.model;

import cz.ivosahlik.ai_ecommerce_support.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Ref_Number")
    private String referenceNumber;
    @Column(name = "PO_Number")
    private String productOrderNumber;
    @Lob
    private String resolutionDetails;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    @OneToOne
    @JoinColumn(name = "conversation_Id")
    private Conversation conversation;
}
