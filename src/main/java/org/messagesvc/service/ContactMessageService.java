package org.messagesvc.service;


import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import org.messagesvc.model.ContactMessage;
import org.messagesvc.repository.ContactMessageRepository;
import org.messagesvc.web.dto.ContactRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ContactMessageService {

    private final JavaMailSender javaMailSender;
    private final ContactMessageRepository repository;

    private final String COMPANY_EMAIL="gbuzunova13@gmail.com";


    public ContactMessageService(JavaMailSender javaMailSender, ContactMessageRepository repository) {
        this.javaMailSender = javaMailSender;
        this.repository = repository;
    }

    public ContactMessage processContactMessage(ContactRequest request) {

       ContactMessage message = ContactMessage.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .name(request.getName())
                .message(request.getMessage())
                .subject("Contact_Form")
                .createdAt(LocalDateTime.now())
                .build();


        sendEmailToCompany(message);
        sendAutoReplyToUser(message);
        message.setProcessed(true);


        return  repository.save(message);
    }

    @Async
    public void sendEmailToCompany(ContactMessage message) {


        try {
            log.info("📧 Preparing to send email to company: " + "gbuzunova");

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


            helper.setTo(COMPANY_EMAIL);
            helper.setSubject("New Contact Form: " + message.getSubject());
            helper.setText(buildCompanyEmailContent(message), true);

            javaMailSender.send(mimeMessage);
            log.info("✅ Email sent to company successfully!");
            this.repository.save(message);

        } catch (Exception e) {
           log.error("❌ Failed to send email to company: " + e.getMessage());
        }
    }

    @Async
    public void sendAutoReplyToUser(ContactMessage message) {
        try {
            log.info("📧 Preparing auto-reply to user: " +message.getEmail());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(message.getEmail());
            helper.setSubject("Thank you for contacting EcoSpace");
            helper.setText(buildAutoReplyContent(message), true);

            javaMailSender.send(mimeMessage);

            System.out.println("✅ Auto-reply sent to user successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to send auto-reply: " + e.getMessage());
        }
    }

    private String buildCompanyEmailContent(ContactMessage message) {
        return "<h2>New Contact Form Submission</h2>" +
                "<p><strong>From:</strong> " + message.getName() + "</p>" +
                "<p><strong>Email:</strong> " + message.getEmail() + "</p>" +
                "<p><strong>Phone:</strong> " + (message.getPhone() != null ? message.getPhone() : "Not provided") + "</p>" +
                "<p><strong>Subject:</strong> " + message.getSubject() + "</p>" +
                "<p><strong>Message:</strong></p>" +
                "<p>" + message.getMessage().replace("\n", "<br>") + "</p>" +
                "<hr><p><em>Received at: " + message.getCreatedAt() + "</em></p>";
    }

    private String buildAutoReplyContent(ContactMessage message) {
        return "<h2>Thank you for contacting EcoSpace!</h2>" +
                "<p>Dear " +message.getName() + ",</p>" +
                "<p>We have received your message and will get back to you within 24 hours.</p>" +
                "<p><strong>Your message:</strong><br>" + message.getEmail() + "</p>" +
                "<hr>" +
                "<p>Best regards,<br>EcoSpace Team</p>";
    }
}


