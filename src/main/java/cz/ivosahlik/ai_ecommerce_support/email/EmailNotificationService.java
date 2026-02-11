package cz.ivosahlik.ai_ecommerce_support.email;

import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import cz.ivosahlik.ai_ecommerce_support.model.Ticket;
import cz.ivosahlik.ai_ecommerce_support.service.customer.ICustomerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final EmailService emailService;
    private final ICustomerService customerService;

    public void sendTicketNotificationEmail(Ticket ticket){
        Customer customer = customerService.getCustomerByEmail(ticket.getConversation().getCustomer().getEmailAddress());
        String customerName = customer.getFullName();
        String customerEmail = customer.getEmailAddress();
        String customerPhone = customer.getPhoneNumber();
        String senderName = "Ecommerce support service";
        String subject = "Support Ticket Created";
        String ticketDetails = ticket.getConversation().getConversationSummary();
        String ticketTile = ticket.getConversation().getConversationTitle();
        String referenceNumber = ticket.getReferenceNumber();
        String htmlBody = null;
        try {
            htmlBody = loadEmailTemplate(customerName, customerEmail, customerPhone, ticketDetails, ticketTile, referenceNumber);
        } catch (IOException e) {
            log.error("Error loading email template : {} ", e.getMessage());
        }
        try {
            emailService.sendNotificationEmail(customerEmail, subject, senderName, htmlBody);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email notification : {} ", e.getMessage());
        }
    }

    public String loadEmailTemplate(String customerName,
                                    String customerEmail,
                                    String customerPhone,
                                    String ticketDetails,
                                    String ticketTile,
                                    String referenceNumber) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/ticket-notification-template.html");
        String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        template = template.replace("{{customerName}}", customerName);
        template = template.replace("{{customerEmail}}", customerEmail);
        template = template.replace("{{customerPhone}}", customerPhone);
        template = template.replace("{{ticketTile}}", ticketTile);
        template = template.replace("{{ticketDetails}}", ticketDetails);
        template = template.replace("{{ticketReferenceNumber}}", referenceNumber);
        return template;
    }

  /*  private static String getHtmlBody(Ticket ticket, String userName, String senderName) {
        String ticketDetails = ticket.getConversation().getConversationSummary();
        String ticketTitle = ticket.getConversation().getConversationTitle();
        String referenceNumber = ticket.getReferenceNumber();
        return "<html>" +
                "<body>" +
                "<p>Dear " + userName + ",</p>" +
                "<p>Thank you for contacting Customer Support. Your support ticket has been created successfully.</p>" +
                "<h3>Ticket Details:</h3>" +
                "<ul>" +
                "<li><strong>Reference Number:</strong> " + referenceNumber + "</li>" +
                "<li><strong>Title:</strong> " + ticketTitle + "</li>" +
                "<li><strong>Description:</strong> " + ticketDetails + "</li>" +
                "</ul>" +
                "<p>If you have any questions, feel free to reply to this email or contact us at support@example.com.</p>" +
                "<p>Best regards,<br/>" + senderName + "</p>" +
                "</body>" +
                "</html>";
    }*/

}
