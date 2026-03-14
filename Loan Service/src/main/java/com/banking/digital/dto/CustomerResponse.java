package com.banking.digital.dto;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private int age;
}
