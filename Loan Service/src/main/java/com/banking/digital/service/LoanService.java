package com.banking.digital.service;

import com.banking.digital.dto.LoanRequest;
import com.banking.digital.dto.LoanResponse;
import com.banking.digital.entity.Loan;
import com.banking.digital.entity.LoanStatus;
import com.banking.digital.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    public LoanResponse apply(LoanRequest request) {

        double rate = 10.0; // fixed 10% interest
        BigDecimal emi = calculateEmi(request.getAmount(), rate, request.getTenure());

        Loan loan = new Loan();
        loan.setCustomerId(request.getCustomerId());
        loan.setAmount(request.getAmount());
        loan.setTenure(request.getTenure());
        loan.setInterestRate(rate);
        loan.setEmi(emi);
        loan.setStatus(LoanStatus.APPLIED);
        loan.setLoanType(request.getLoanType());

        repository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getAmount(),
                loan.getTenure(),
                loan.getEmi(),
                loan.getStatus().name()
        );
    }
    public LoanResponse approve(Long loanId) {

        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus(LoanStatus.APPROVED);
        repository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getAmount(),
                loan.getTenure(),
                loan.getEmi(),
                loan.getStatus().name()
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

