package org.messagesvc.web;



import org.messagesvc.model.ContactMessage;
import org.messagesvc.service.ContactMessageService;
import org.messagesvc.web.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact")

public class ContactController {

    @Autowired
    private final ContactMessageService contactMessageService;

    public ContactController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<ContactRequest>sendMessage(@RequestBody ContactRequest request){

        ContactMessage message= contactMessageService.processContactMessage(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);

    }
}
