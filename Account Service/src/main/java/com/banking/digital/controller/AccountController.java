package com.banking.digital.controller;

import com.banking.digital.dto.BalanceResponse;
import com.banking.digital.dto.CreateAccountRequest;
import com.banking.digital.dto.TransactionRequest;
import com.banking.digital.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService service;

    @PostMapping("/create")
    public String createAccount(@RequestBody CreateAccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance(@RequestParam("accountNumber") String accountNumber) {
        return service.getBalance(accountNumber);
    }

    @PutMapping("/deposit")
    public String deposit(@RequestBody TransactionRequest request) {
        return service.deposit(request);
    }

    @PutMapping("/withdraw")
    public String withdraw(@RequestBody TransactionRequest request) {
        return service.withdraw(request);
    }

}
