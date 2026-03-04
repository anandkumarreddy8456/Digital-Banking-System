package com.banking.digital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private Long customerId;

    private String accountHolderName;

    private int accountHolderAge;

    private String accountType; // SAVINGS / CURRENT

    private BigDecimal balance;

    @Column(unique = true)
    private String mobileNumber;

    private String status; // ACTIVE / BLOCKED

    private LocalDateTime createdAt = LocalDateTime.now();
}
