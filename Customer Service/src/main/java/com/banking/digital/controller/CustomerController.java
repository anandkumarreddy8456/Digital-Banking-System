package com.banking.digital.controller;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.CustomerRequest;
import com.banking.digital.dto.CustomerResponse;
import com.banking.digital.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@RequestBody CustomerRequest request) {
        return service.create(request);
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@RequestParam Long id) {
        return service.getById(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        return service.getAll();
    }
}
