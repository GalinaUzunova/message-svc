package org.messagesvc.config;

import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class RestTemplateConfig {

    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
