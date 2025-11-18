package org.messagesvc.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.messagesvc.model.ContactMessage;
import org.messagesvc.repository.ContactMessageRepository;
import org.messagesvc.web.dto.ContactRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @CacheEvict(value = "messages",allEntries = true)
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

    @CacheEvict(value = "messages",allEntries = true)
    public void sendEmailToCompany(ContactMessage message) {

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(COMPANY_EMAIL);
            helper.setSubject("New Contact Form: " + message.getSubject());
            helper.setText(buildCompanyEmailContent(message), true);

            javaMailSender.send(mimeMessage);
            this.repository.save(message);

        } catch (MailSendException e) {
            log.error("❌ Failed to send email to company: " + e.getMessage());
            throw  new MailSendException("Failed to send contact email to company", e);

        } catch (MessagingException e) {
            log.error("Unexpected error sending email to company: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Async
    @CacheEvict(value = "messages",allEntries = true)
    public void sendAutoReplyToUser(ContactMessage message) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(message.getEmail());
            helper.setSubject("Thank you for contacting EcoSpace");
            helper.setText(buildAutoReplyContent(message), true);

            javaMailSender.send(mimeMessage);


        } catch (Exception e) {
            log.error("❌ Failed to send auto-reply: " + e.getMessage());
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
    @Cacheable("messages")
    public List<ContactRequest>getAllMessages(){
     return    this.repository.findAllByOrderByCreatedAtDesc().stream().map(this::convertToDto)
                .collect(Collectors.toList());

    }

    private ContactRequest convertToDto(ContactMessage entity) {
        ContactRequest dto = new ContactRequest();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setId(entity.getId());

        return dto;
    }
    @Cacheable("messages")
    public List<ContactRequest>getAllMessagesByToday(){
       List<ContactMessage>sentToday=repository.findByCreatedAtDate(LocalDate.now());
       return sentToday.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @CacheEvict(value = "messages",allEntries = true)
    public void deleteByID(UUID id) {
        Optional<ContactMessage> message = this.repository.getAllById(id);
        if(message.isEmpty()){
            throw new RuntimeException("No messages!");
        }
       this.repository.delete(message.get());


    }
}


