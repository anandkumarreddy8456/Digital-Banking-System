package com.banking.digital.service;

import com.banking.digital.dto.AccountTransactionRequest;
import com.banking.digital.dto.TransferRequest;
import com.banking.digital.entity.Transaction;
import com.banking.digital.entity.TransactionStatus;
import com.banking.digital.feign.AccountClient;
import com.banking.digital.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountClient accountClient;

    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackTransfer")
    public String transfer(TransferRequest request) {

        if (repository.existsByReferenceId(request.getReferenceId())) {
            return "Duplicate Transaction Request";
        }

        Transaction txn = new Transaction();
        txn.setReferenceId(request.getReferenceId());
        txn.setFromAccount(request.getFromAccount());
        txn.setToAccount(request.getToAccount());
        txn.setAmount(request.getAmount());
        txn.setStatus(TransactionStatus.INITIATED);
        repository.save(txn);

        AccountTransactionRequest withdrawReq = new AccountTransactionRequest();
        withdrawReq.setAccountNumber(request.getFromAccount());
        withdrawReq.setAmount(request.getAmount());

        accountClient.withdraw(withdrawReq);

        AccountTransactionRequest depositReq = new AccountTransactionRequest();
        depositReq.setAccountNumber(request.getToAccount());
        depositReq.setAmount(request.getAmount());

        accountClient.deposit(depositReq);

        txn.setStatus(TransactionStatus.SUCCESS);
        repository.save(txn);

        return "Transaction Successful";
    }
    public String fallbackTransfer(TransferRequest request, Throwable ex) {

        Transaction txn = new Transaction();
        txn.setReferenceId(request.getReferenceId());
        txn.setFromAccount(request.getFromAccount());
        txn.setToAccount(request.getToAccount());
        txn.setAmount(request.getAmount());
        txn.setStatus(TransactionStatus.FAILED);

        repository.save(txn);

        return "Transaction Failed";
    }
}
