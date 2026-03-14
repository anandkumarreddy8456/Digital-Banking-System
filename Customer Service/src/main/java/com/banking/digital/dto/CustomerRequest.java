package com.banking.digital.dto;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private int age;
    private String email;
    private String mobile;
    private String address;
}
