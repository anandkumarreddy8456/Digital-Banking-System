package com.banking.digital.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private Long customerId;
    private String mobileNumber;
    private String accountType;
    private BigDecimal initialDeposit;
}
