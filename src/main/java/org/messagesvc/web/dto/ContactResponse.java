package org.messagesvc.web.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
    private UUID id;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createdAt;
}
