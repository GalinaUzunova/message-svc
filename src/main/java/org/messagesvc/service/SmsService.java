package org.messagesvc.service;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;

import org.messagesvc.web.dto.SubscriptionRemainderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

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

        } catch (Exception e) {
            System.err.println(" Twilio initialization failed");
        }
    }

    public void sendSms(String message, String toPhone) {
        try {
            Message.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(fromPhone),
                    message
            ).create();

            System.out.println("SMS sent successfully!");

        } catch (Exception e) {
            System.out.println("ERROR sending SMS: " + e.getMessage());

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



