package com.banking.digital.dto;

import com.banking.digital.entity.CardType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardRequest {
    private String accountNumber;
    private CardType cardType;
    private BigDecimal limitAmount;
}
