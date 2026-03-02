package com.banking.digital.dto;

import com.banking.digital.entity.LoanType;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoanRequest {

    private Long customerId;
    private BigDecimal amount;
    private Integer tenure;
    private LoanType loanType;
}
