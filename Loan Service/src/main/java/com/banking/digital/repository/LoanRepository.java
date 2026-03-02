package com.banking.digital.repository;

import com.banking.digital.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {

    List<Loan> findByCustomerId(Long customerId);
}
