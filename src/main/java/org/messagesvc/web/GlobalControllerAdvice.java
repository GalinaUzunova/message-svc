package org.messagesvc.web;

import lombok.extern.slf4j.Slf4j;
import org.messagesvc.exception.SmsDeliveryException;
import org.messagesvc.exception.TwilioInitException;
import org.messagesvc.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
 @ExceptionHandler(TwilioInitException.class)
    public ResponseEntity<ErrorResponse> handleTwilioInitialization(
            TwilioInitException ex) {

        log.error("Twilio service unavailable: {}", ex.getMessage());
        org.messagesvc.web.dto.ErrorResponse dto=new org.messagesvc.web.dto.ErrorResponse(LocalDateTime.now(),
                "SERVICE_UNAVAILABLE" + " " +
                "SMS service is currently unavailable. Please try again later.");


        return
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }
    @ExceptionHandler(SmsDeliveryException.class)
    public ResponseEntity<ErrorResponse>handleNotSendingSms(SmsDeliveryException ex){

        log.error("Cant send the sms: {}", ex.getMessage());
        ErrorResponse dto=new ErrorResponse(LocalDateTime.now(),"SMS service is currently unavailable. Please try again later.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);

    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ErrorResponse>handleNotSendEmail(MailSendException ex){

     ErrorResponse dto=new ErrorResponse(LocalDateTime.now(),"Email service is temporarily unavailable. Please try again later.");
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dto);
    }
}
