package com.banking.digital.service;

import com.banking.digital.dto.CardRequest;
import com.banking.digital.dto.CardResponse;
import com.banking.digital.entity.Card;
import com.banking.digital.entity.CardStatus;
import com.banking.digital.entity.CardType;
import com.banking.digital.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository repository;

    public CardResponse issueCard(CardRequest request) {

        Card card = new Card();

        card.setCardNumber(generateCardNumber());
        card.setAccountNumber(request.getAccountNumber());
        card.setCardType(request.getCardType());
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(LocalDate.now().plusYears(5));

        if (request.getCardType() == CardType.CREDIT) {
            card.setLimitAmount(request.getLimitAmount());
        }

        repository.save(card);

        return new CardResponse(
                card.getCardNumber(),
                card.getCardType().name(),
                card.getStatus().name()
        );
    }
    public String blockCard(String cardNumber) {

        Card card = repository.findAll().stream()
                .filter(c -> c.getCardNumber().equals(cardNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setStatus(CardStatus.BLOCKED);
        repository.save(card);

        return "Card Blocked Successfully";
    }
    private String generateCardNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
