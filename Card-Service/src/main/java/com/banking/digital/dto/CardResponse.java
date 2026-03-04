package com.banking.digital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponse {
    private String cardNumber;
    private String cardType;
    private String status;
    private String mobileNumber;
}
