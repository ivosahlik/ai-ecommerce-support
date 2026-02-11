package cz.ivosahlik.ai_ecommerce_support.controller;

import cz.ivosahlik.ai_ecommerce_support.dtos.TicketDTO;
import cz.ivosahlik.ai_ecommerce_support.service.ticket.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tickets")
public class TicketController {

    private final ITicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<TicketDTO> resolveTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        String resolutionDetails = requestBody.get("resolutionDetails");
        TicketDTO resolvedTicket = ticketService.resolveTicket(id, resolutionDetails);
        return ResponseEntity.ok(resolvedTicket);
    }
}
