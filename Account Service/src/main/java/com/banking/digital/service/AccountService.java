package com.banking.digital.service;

import com.banking.digital.dto.BalanceResponse;
import com.banking.digital.dto.CreateAccountRequest;
import com.banking.digital.dto.TransactionRequest;
import com.banking.digital.entity.Account;
import com.banking.digital.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public String createAccount(CreateAccountRequest request) {

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10));
        account.setCustomerId(request.getCustomerId());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());
        account.setStatus("ACTIVE");

        repository.save(account);

        return "Account Created Successfully";
    }
    public BalanceResponse getBalance(String accountNumber) {

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new BalanceResponse(account.getAccountNumber(), account.getBalance());
    }
    public String deposit(TransactionRequest request) {

        Account account = repository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        repository.save(account);

        return "Amount Deposited Successfully";
    }
    public String withdraw(TransactionRequest request) {

        Account account = repository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        repository.save(account);

        return "Amount Withdrawn Successfully";
    }
}
