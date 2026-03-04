package com.banking.digital.service;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.common.ConstantMessages;
import com.banking.digital.dto.BalanceResponse;
import com.banking.digital.dto.CreateAccountRequest;
import com.banking.digital.dto.TransactionRequest;
import com.banking.digital.entity.Account;
import com.banking.digital.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public ResponseEntity<ApiResponse<String>> createAccount(CreateAccountRequest request) {
        try {
            if(request.getAccountHolderAge()<18){
                return new ResponseEntity<>(
                        new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ConstantMessages.ageIsNotValid, null),
                        HttpStatus.BAD_REQUEST
                );
            }
            if (repository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
                return new ResponseEntity<>(
                        new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ConstantMessages.mobileAlreadyAllocated, null),
                        HttpStatus.BAD_REQUEST
                );
            }
            String accountNumber;
            do {
                accountNumber = String.valueOf((long) (Math.random() * 1_000_000_0000L)); // 10-digit number
            } while (repository.findByAccountNumber(accountNumber).isPresent());
            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setAccountHolderName(request.getAccountHolderName());
            account.setAccountHolderAge(request.getAccountHolderAge());
            account.setMobileNumber(request.getMobileNumber());
            account.setAccountType(request.getAccountType());
            account.setCustomerId(request.getCustomerId());
            account.setStatus(ConstantMessages.active);
            account.setBalance(request.getInitialDeposit() != null ? request.getInitialDeposit() : BigDecimal.ZERO);
            repository.save(account);

            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.CREATED.value(), ConstantMessages.accountCreatedSuccessfully, accountNumber),
                    HttpStatus.CREATED
            );

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(String accountNumber) {
        try {
            Account account = repository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.accountNotFound));

            BalanceResponse balanceResponse = new BalanceResponse(
                    account.getAccountNumber(),
                    account.getBalance()
            );

            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.balanceFetchedSuccessfully, balanceResponse),
                    HttpStatus.ACCEPTED
            );

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    public ResponseEntity<ApiResponse<String>> deposit(TransactionRequest request) {
        try {
            Account account = repository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.accountNotFound));

            account.setBalance(account.getBalance().add(request.getAmount()));
            repository.save(account);

            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.amountDepositSuccessfully, ""),
                    HttpStatus.ACCEPTED
            );

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage().toUpperCase(), null),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    public ResponseEntity<ApiResponse<String>> withdraw(TransactionRequest request) {
        try {
            Account account = repository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.accountNotFound));

            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException(ConstantMessages.insufficientBalance);
            }

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            repository.save(account);

            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.amountWithdrawnSuccessfully, ""),
                    HttpStatus.ACCEPTED
            );

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ConstantMessages.insufficientBalance, null),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
