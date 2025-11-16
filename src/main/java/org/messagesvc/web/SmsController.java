package org.messagesvc.web;

import org.messagesvc.service.SmsService;
import org.messagesvc.web.dto.SubscriptionRemainderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SmsController {

 private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/sms/subscription-reminders")
    public ResponseEntity<String> sendSubscriptionReminder(@RequestBody SubscriptionRemainderRequest request) {

        smsService.messageRemainder(request);
        return ResponseEntity.ok("Reminder sent");
    }




    }
