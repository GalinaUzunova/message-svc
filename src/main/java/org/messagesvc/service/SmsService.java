package org.messagesvc.service;

import com.twilio.rest.conversations.v1.conversation.Message;
import lombok.extern.slf4j.Slf4j;
import org.messagesvc.web.dto.SubscriptionRemainderRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@Slf4j
public class SmsService {

    public void sendSms(String message, String phone) {
        try {
          Message.creator(phone)
                    .setDateCreated(ZonedDateTime.now())
                    .setBody(message)
                    .setAuthor("Ecospace")
                    .create();

            log.info("SMS sent successfully!");

        } catch (Exception e) {
            log.error("ERROR sending SMS: " + e.getMessage());

        }

    }

    public void messageRemainder(SubscriptionRemainderRequest request) {

        String message = String.format(
                "Hi %s, your '%s' subscription expires on %s. Renew now to continue service!",
                request.getUsername(),
                request.getSubsName(),
                request.getExpiredOn());

        sendSms(message, request.getPhone());
    }

}



