package com.banking.digital.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountTransactionRequest {
    private String accountNumber;
    private BigDecimal amount;
}
