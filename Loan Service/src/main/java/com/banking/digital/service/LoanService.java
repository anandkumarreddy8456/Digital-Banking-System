package com.banking.digital.service;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.common.ConstantMessages;
import com.banking.digital.dto.CustomerResponse;
import com.banking.digital.dto.LoanRequest;
import com.banking.digital.dto.LoanResponse;
import com.banking.digital.entity.Loan;
import com.banking.digital.entity.LoanStatus;
import com.banking.digital.feign.AccountClient;
import com.banking.digital.feign.CustomerClient;
import com.banking.digital.feign.NotificationClient;
import com.banking.digital.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private AccountClient accountClient;


    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private NotificationClient notificationClient;

    public ResponseEntity<ApiResponse<LoanResponse>> apply(LoanRequest request) {
        ApiResponse<String> accountResponse =
                accountClient.getAccountNumberByMobile(request.getMobileNumber());
        CustomerResponse customerResponse=customerClient.getCustomerById(request.getCustomerId()).getData();
        if(customerResponse == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ConstantMessages.customerIdNotMatched, null),
                    HttpStatus.BAD_REQUEST
            );

        }
        String accountNumber = accountResponse.getData();
        if(accountNumber == null){
           return new ResponseEntity<>( new ApiResponse<LoanResponse>(HttpStatus.NOT_FOUND.value(), ConstantMessages.accountNotFound,null),HttpStatus.NOT_FOUND);
        }
        double rate = 10.0; // fixed 10% interest
        BigDecimal emi = calculateEmi(request.getAmount(), rate, request.getTenure());

        Loan loan = new Loan();
        loan.setCustomerId(customerResponse.getId());
        loan.setMobileNumber(request.getMobileNumber());
        loan.setAccountNumber(accountNumber);
        loan.setAmount(request.getAmount());
        loan.setTenure(request.getTenure());
        loan.setInterestRate(rate);
        loan.setEmi(emi);
        loan.setStatus(LoanStatus.APPLIED);
        loan.setLoanType(request.getLoanType());

        repository.save(loan);
        // 4️⃣ Send Notification
        Map<String, String> notification = new HashMap<>();
        notification.put("recipient", accountNumber);
        notification.put("message", "Loan application submitted successfully");
        notification.put("type", "EMAIL");

        notificationClient.sendNotification(notification);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.loanCreated, new LoanResponse(
                loan.getId(),
                loan.getAmount(),
                loan.getTenure(),
                loan.getEmi(),
                loan.getStatus().name()
        )), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse<LoanResponse>> approve(Long loanId) {

        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new RuntimeException(ConstantMessages.loanNotFound));

        // Update Loan Status
        loan.setStatus(LoanStatus.APPROVED);
        repository.save(loan);

        // Send Notification
        Map<String, String> notification = new HashMap<>();
        notification.put("recipient", loan.getAccountNumber());
        notification.put("message", "Your loan has been approved successfully");
        notification.put("type", "EMAIL");

        notificationClient.sendNotification(notification);

        // Prepare Response
        LoanResponse loanResponse = new LoanResponse(
                loan.getId(),
                loan.getAmount(),
                loan.getTenure(),
                loan.getEmi(),
                loan.getStatus().name()
        );

        return new ResponseEntity<>(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        ConstantMessages.loanApproved,
                        loanResponse
                ),
                HttpStatus.OK
        );
    }

    private BigDecimal calculateEmi(BigDecimal principal, double rate, int tenure) {

        double monthlyRate = rate / (12 * 100);
        double emi = (principal.doubleValue() * monthlyRate *
                Math.pow(1 + monthlyRate, tenure)) /
                (Math.pow(1 + monthlyRate, tenure) - 1);

        return BigDecimal.valueOf(emi);
    }
}

