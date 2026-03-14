package com.banking.digital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private int customerAge;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String address;

    private LocalDateTime createdAt = LocalDateTime.now();
}
