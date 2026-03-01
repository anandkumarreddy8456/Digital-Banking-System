package com.banking.digital.controller;

import com.banking.digital.dto.TransferRequest;
import com.banking.digital.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return service.transfer(request);
    }
}
