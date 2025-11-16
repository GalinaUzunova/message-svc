package org.messagesvc.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}
