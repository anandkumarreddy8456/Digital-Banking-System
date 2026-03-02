package com.banking.digital.repository;

import com.banking.digital.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    boolean existsByReferenceId(String referenceId);

    Optional<Transaction> findByReferenceId(String referenceId);

    List<Transaction> findByFromAccountOrToAccount(String from, String to);
}
