package org.messagesvc.service;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.messagesvc.web.dto.SubscriptionRemainderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;
    @Value("${twilio.auth-token}")
    private String authToken;
    @Value("${twilio.phone-number}")
    private String fromPhone;


    @PostConstruct
    public void init() {
        try {
            Twilio.init(accountSid,authToken);
            log.info("✅ Twilio initialized successfully for account: {}", accountSid);
        } catch (Exception e) {
            log.error("❌ Twilio initialization failed: {}", e.getMessage());
        }
    }

    public void sendSms(String message, String toPhone) {
        try {
            Message twilioMessage = Message.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(fromPhone),
                    message
            ).create();

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



