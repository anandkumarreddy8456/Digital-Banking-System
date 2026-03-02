package com.banking.digital.repository;

import com.banking.digital.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findByAccountNumber(String accountNumber);
}
