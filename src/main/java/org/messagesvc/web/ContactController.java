package org.messagesvc.web;



import org.messagesvc.model.ContactMessage;
import org.messagesvc.service.ContactMessageService;
import org.messagesvc.web.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact")

public class ContactController {

    @Autowired
    private final ContactMessageService contactMessageService;

    public ContactController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<ContactRequest> sendMessage(@RequestBody ContactRequest request) {

        ContactMessage message = contactMessageService.processContactMessage(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);

    }

    @GetMapping("/contact-messages")
    public ResponseEntity<List<ContactRequest>> viewMessages() {

        List<ContactRequest> allMessages = contactMessageService.getAllMessages();

        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/contact-messages/today")
    public ResponseEntity<List<ContactRequest>> todayMessages() {

        List<ContactRequest> allMessagesByToday = contactMessageService.getAllMessagesByToday();

        return ResponseEntity.ok(allMessagesByToday);


    }
    @DeleteMapping("contact-messages/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        try {
            contactMessageService.deleteByID(id);
            return ResponseEntity.ok("Message deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Message not found with id: " + id);
        }
    }
}
