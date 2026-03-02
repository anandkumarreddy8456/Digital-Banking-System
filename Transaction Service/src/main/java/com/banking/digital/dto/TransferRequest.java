package com.banking.digital.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    private String referenceId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}
