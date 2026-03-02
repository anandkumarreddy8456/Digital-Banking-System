package com.banking.digital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class LoanResponse {

    private Long loanId;
    private BigDecimal amount;
    private Integer tenure;
    private BigDecimal emi;
    private String status;
}
