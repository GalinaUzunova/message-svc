package org.messagesvc.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsRequest {

    private String toPhone;

    private String message;
}
