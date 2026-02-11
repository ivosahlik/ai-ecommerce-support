package cz.ivosahlik.ai_ecommerce_support.repository;

import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
