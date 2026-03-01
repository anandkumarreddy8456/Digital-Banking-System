package com.banking.digital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse {
    private String accountNumber;
    private BigDecimal balance;
}
