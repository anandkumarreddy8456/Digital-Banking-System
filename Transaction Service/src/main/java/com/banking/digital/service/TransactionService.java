package com.banking.digital.service;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.common.ConstantMessages;
import com.banking.digital.dto.AccountTransactionRequest;
import com.banking.digital.dto.TransferRequest;
import com.banking.digital.entity.Transaction;
import com.banking.digital.entity.TransactionStatus;
import com.banking.digital.feign.AccountClient;
import com.banking.digital.feign.NotificationClient;
import com.banking.digital.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private NotificationClient notificationClient;

    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackTransfer")
    public ResponseEntity<ApiResponse<String>> transfer(TransferRequest request) {

        if (repository.existsByReferenceId(request.getReferenceId())) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ConstantMessages.duplicateTrsReq,ConstantMessages.transactionId),HttpStatus.BAD_REQUEST);
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
        // Send Notification
        Map<String, String> notification = new HashMap<>();
        notification.put("recipient", request.getFromAccount());
        notification.put("message", "Transaction Successful: ₹" + request.getAmount());
        notification.put("type", "EMAIL");

        notificationClient.sendNotification(notification);
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
