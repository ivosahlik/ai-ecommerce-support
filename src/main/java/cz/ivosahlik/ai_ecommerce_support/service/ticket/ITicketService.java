package cz.ivosahlik.ai_ecommerce_support.service.ticket;

import cz.ivosahlik.ai_ecommerce_support.dtos.TicketDTO;
import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;

import java.util.List;

public interface ITicketService {
    Ticket createTicketForConversation(Conversation conversation);
    TicketDTO getTicketById(Long ticketId);
    TicketDTO resolveTicket(Long ticketId, String resolutionDetails);
    List<TicketDTO> getAllTickets();
}
