package cz.ivosahlik.ai_ecommerce_support.service.ticket;

import cz.ivosahlik.ai_ecommerce_support.dtos.TicketDTO;
import cz.ivosahlik.ai_ecommerce_support.enums.TicketStatus;
import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import cz.ivosahlik.ai_ecommerce_support.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService{
    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;

  /*  @Override
    public Ticket createTicketForConversation(Conversation conversation) {
        Ticket ticket = new Ticket();
        ticket.setConversation(conversation);
        ticket.setTicketStatus(TicketStatus.OPENED);
        ticket.setResolvedAt(null);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setReferenceNumber(generateRandomAlphanumeric());
        return ticketRepository.save(ticket);
    }*/

    @Override
    public Ticket createTicketForConversation(Conversation conversation) {
        Ticket ticket = new Ticket();
        ticket.setConversation(conversation);
        ticket.setTicketStatus(TicketStatus.OPENED);
        ticket.setResolvedAt(null);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setReferenceNumber(generateRandomAlphanumeric());
        return ticket;
    }

    @Override
    public TicketDTO getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> objectMapper.convertValue(ticket, TicketDTO.class))
                .orElseThrow(()  -> new EntityNotFoundException("Ticket with id " + ticketId + " not found"));
    }

    @Override
    public TicketDTO resolveTicket(Long ticketId, String resolutionDetails) {
        if (resolutionDetails == null || resolutionDetails.isEmpty()) {
            throw new IllegalArgumentException("Resolution details cannot be empty");
        }
        return ticketRepository.findById(ticketId).map(ticket -> {
            ticket.setResolutionDetails(resolutionDetails);
            ticket.setResolvedAt(LocalDateTime.now());
            ticket.setTicketStatus(TicketStatus.RESOLVED);
            Ticket updatedTicket = ticketRepository.save(ticket);
            return objectMapper.convertValue(updatedTicket, TicketDTO.class);
        }).orElseThrow(()  -> new EntityNotFoundException("Ticket with id " + ticketId + " not found"));
    }

    @Override
    public List<TicketDTO> getAllTickets() {
        return  ticketRepository.findAll()
                .stream()
                .map(ticket -> objectMapper.convertValue(ticket, TicketDTO.class))
                .collect(Collectors.toList());
    }

    private String generateRandomAlphanumeric() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z').filteredBy(Character::isLetterOrDigit)
                .get();
        return generator.generate(10).toUpperCase();
    }

}
