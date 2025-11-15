package org.messagesvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MessageSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageSvcApplication.class, args);

        System.out.println("🚀 Message Microservice started on port 8082");
    }

}
