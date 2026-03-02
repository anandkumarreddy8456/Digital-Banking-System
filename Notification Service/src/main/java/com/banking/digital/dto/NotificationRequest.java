package com.banking.digital.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private String recipient;
    private String message;
    private String type;
}
