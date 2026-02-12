package cz.ivosahlik.ai_ecommerce_support.event;

import cz.ivosahlik.ai_ecommerce_support.email.EmailNotificationService;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

// old way. better way in TicketCreationAnnotationEventListener
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class TicketCreationEventListener implements ApplicationListener<TicketCreationEvent> {
//    private final EmailNotificationService emailNotificationService;
//
//    @Override
//    public void onApplicationEvent(TicketCreationEvent event) {
//        Ticket ticket = event.getTicket();
//        try {
//            emailNotificationService.sendTicketNotificationEmail(ticket);
//        } catch (MessagingException e) {
//            log.error("Failed to send ticket notification email to the customer: {}", e.getMessage());
//        }
//    }
//}
