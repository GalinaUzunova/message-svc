package org.messagesvc.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionRemainderRequest {

    private String username;
    private String phone;
    private String subsName;
    private LocalDateTime expiredOn;
}
