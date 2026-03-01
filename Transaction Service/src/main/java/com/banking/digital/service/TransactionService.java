package com.banking.digital.service;

import com.banking.digital.dto.TransferRequest;
import com.banking.digital.entity.Transaction;
import com.banking.digital.feign.AccountClient;
import com.banking.digital.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountClient accountClient;

    public String transfer(TransferRequest request) {

        try {

            // Withdraw from sender
            accountClient.withdraw(request);

            // Deposit to receiver
            accountClient.deposit(request);

            Transaction txn = new Transaction();
            txn.setFromAccount(request.getFromAccount());
            txn.setToAccount(request.getToAccount());
            txn.setAmount(request.getAmount());
            txn.setStatus("SUCCESS");

            repository.save(txn);

            return "Transaction Successful";

        } catch (Exception e) {

            Transaction txn = new Transaction();
            txn.setFromAccount(request.getFromAccount());
            txn.setToAccount(request.getToAccount());
            txn.setAmount(request.getAmount());
            txn.setStatus("FAILED");

            repository.save(txn);

            return "Transaction Failed";
        }
    }
}
