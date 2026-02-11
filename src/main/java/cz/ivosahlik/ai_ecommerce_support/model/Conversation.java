package cz.ivosahlik.ai_ecommerce_support.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String conversationTitle;
    @Lob
    private String conversationSummary;
    private boolean ticketCreated;

    @ManyToOne
    private Customer customer;

    @JsonIgnore
    @OneToOne(mappedBy = "conversation")
    private Ticket ticket;
}
