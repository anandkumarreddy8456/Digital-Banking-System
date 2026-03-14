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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
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
        // Check duplicate reference
        if (repository.existsByReferenceId(request.getReferenceId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            ConstantMessages.duplicateTrsReq,
                            ConstantMessages.transactionId));
        }

        try {
            // Validate accounts
            ApiResponse<String> fromResponse = accountClient.getAccountNumberByAccountNumber(request.getFromAccount());
            if (fromResponse == null || fromResponse.getApiResponse() == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                                ConstantMessages.invalidWithdraw,
                                ConstantMessages.properFromAccount));
            }

            ApiResponse<String> toResponse = accountClient.getAccountNumberByAccountNumber(request.getToAccount());
            if (toResponse == null || toResponse.getApiResponse() == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                                ConstantMessages.invalidDeposit,
                                ConstantMessages.properToAccount));
            }

            // Create transaction record
            Transaction txn = new Transaction();
            txn.setReferenceId(request.getReferenceId());
            txn.setFromAccount(request.getFromAccount());
            txn.setToAccount(request.getToAccount());
            txn.setAmount(request.getAmount());
            txn.setStatus(TransactionStatus.INITIATED);
            repository.save(txn);

            // Perform withdraw
            AccountTransactionRequest withdrawReq = new AccountTransactionRequest();
            withdrawReq.setAccountNumber(request.getFromAccount());
            withdrawReq.setAmount(request.getAmount());
            accountClient.withdraw(withdrawReq);

            // Perform deposit
            AccountTransactionRequest depositReq = new AccountTransactionRequest();
            depositReq.setAccountNumber(request.getToAccount());
            depositReq.setAmount(request.getAmount());
            accountClient.deposit(depositReq);

            // Update transaction status
            txn.setStatus(TransactionStatus.SUCCESS);
            repository.save(txn);

            // Send notifications
            Map<String, String> notification = new HashMap<>();
            notification.put("recipient", request.getFromAccount());
            notification.put("message", "Transaction Successful: ₹" + request.getAmount());
            notification.put("type", "EMAIL");
            notificationClient.sendNotification(notification);

            notification.put("recipient", request.getToAccount());
            notification.put("message", "Amount Credited: ₹" + request.getAmount());
            notificationClient.sendNotification(notification);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(),
                            ConstantMessages.transactionSuccess,
                            txn.getReferenceId()));

        } catch (Exception e) {
            // Rollback or mark failed
            Transaction txn = new Transaction();
            txn.setReferenceId(request.getReferenceId());
            txn.setFromAccount(request.getFromAccount());
            txn.setToAccount(request.getToAccount());
            txn.setAmount(request.getAmount());
            txn.setStatus(TransactionStatus.FAILED);
            repository.save(txn);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            ConstantMessages.transactionFail,
                            e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<String>> fallbackTransfer(TransferRequest request, Throwable ex) {

        Transaction txn = new Transaction();
        txn.setReferenceId(request.getReferenceId());
        txn.setFromAccount(request.getFromAccount());
        txn.setToAccount(request.getToAccount());
        txn.setAmount(request.getAmount());
        txn.setStatus(TransactionStatus.FAILED);

        repository.save(txn);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ConstantMessages.transactionFail,ConstantMessages.transactionFail),HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<ApiResponse<Transaction>> getByReference(String referenceId){
        try{
            Transaction txt=repository.findByReferenceId(referenceId)
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.transactionNotFound));
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.transactionSuccess,txt),HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ConstantMessages.transactionNotFound,null),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),ConstantMessages.internalServerError,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ApiResponse<List<Transaction>>> getByAccount(String accountNumber){
        try{

            String accNumber = null;

            ApiResponse<String> fromResponse = accountClient.getAccountNumberByAccountNumber(accountNumber);
            if (fromResponse != null) {
                accNumber = fromResponse.getApiResponse();
            } else {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ConstantMessages.invalidAccount,List.of()),HttpStatus.NOT_FOUND);
            }
            List<Transaction> lis=repository.findByFromAccountOrToAccount(accNumber, accountNumber);

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.transactionSuccess,lis),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),ConstantMessages.internalServerError,List.of()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
