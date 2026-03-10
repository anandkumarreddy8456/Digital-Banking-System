package com.banking.digital.controller;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.TransferRequest;
import com.banking.digital.entity.Transaction;
import com.banking.digital.repository.TransactionRepository;
import com.banking.digital.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepository repository;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<String>> transfer(@RequestBody TransferRequest request) {
        return service.transfer(request);
    }

    @GetMapping("/getByReferenceId")
    public ResponseEntity<ApiResponse<Transaction>> getByReference(@RequestParam("referenceId") String referenceId) {
        return service.getByReference(referenceId);
    }

    @GetMapping("/account/getByAccount")
    public ResponseEntity<ApiResponse<List<Transaction>>> getByAccount(@RequestParam("accountNumber") String accountNumber) {
        return service.getByAccount(accountNumber);
    }
}
