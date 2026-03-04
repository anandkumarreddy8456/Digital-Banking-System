package com.banking.digital.controller;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.BalanceResponse;
import com.banking.digital.dto.CreateAccountRequest;
import com.banking.digital.dto.TransactionRequest;
import com.banking.digital.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createAccount(@RequestBody CreateAccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse< BalanceResponse>> getBalance(@RequestParam("accountNumber") String accountNumber) {
        return service.getBalance(accountNumber);
    }

    @PutMapping("/deposit")
    public ResponseEntity<ApiResponse<String>> deposit(@RequestBody TransactionRequest request) {
        return service.deposit(request);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(@RequestBody TransactionRequest request) {
        return service.withdraw(request);
    }

}
