package com.banking.digital.service;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.common.ConstantMessages;
import com.banking.digital.dto.CardRequest;
import com.banking.digital.dto.CardResponse;
import com.banking.digital.entity.Card;
import com.banking.digital.entity.CardStatus;
import com.banking.digital.entity.CardType;
import com.banking.digital.feign.AccountClient;
import com.banking.digital.feign.NotificationClient;
import com.banking.digital.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository repository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private NotificationClient notificationClient;

    public ResponseEntity<ApiResponse<CardResponse>> issueCard(CardRequest request) {

        ApiResponse<String> accountResponse =
                accountClient.getAccountNumberByMobile(request.getMobileNumber());

        String accountNumber = accountResponse.getData();
        if(accountNumber == null){
            return new ResponseEntity<>( new ApiResponse<CardResponse>(HttpStatus.NOT_FOUND.value(), ConstantMessages.accountNotFound,null),HttpStatus.NOT_FOUND);
        }
        if(!accountNumber.equals(request.getAccountNumber())){
            return new ResponseEntity<>( new ApiResponse<CardResponse>(HttpStatus.NOT_FOUND.value(), ConstantMessages.properAccountNumber,null),HttpStatus.NOT_FOUND);
        }

        Card card = new Card();

        card.setCardNumber(generateCardNumber());
        card.setAccountNumber(request.getAccountNumber());
        card.setCardType(request.getCardType());
        card.setStatus(CardStatus.ACTIVE);
        card.setMobileNumber(request.getMobileNumber());
        card.setExpiryDate(LocalDate.now().plusYears(5));

        if (request.getCardType() == CardType.CREDIT) {
            card.setLimitAmount(request.getLimitAmount());
        }

        repository.save(card);
        Map<String, String> notification = new HashMap<>();
        notification.put("recipient", accountNumber);
        notification.put("message", "Loan application submitted successfully");
        notification.put("type", "EMAIL");

        notificationClient.sendNotification(notification);

        return new ResponseEntity<>(new ApiResponse<CardResponse>(HttpStatus.ACCEPTED.value(), ConstantMessages.accountCreated,new CardResponse(
                card.getCardNumber(),
                card.getCardType().name(),
                card.getStatus().name(),
                card.getMobileNumber())
        ),HttpStatus.ACCEPTED);
    }
    public ResponseEntity<ApiResponse<String>> blockCard(String cardNumber) {
        try {
            Card card = repository.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.cardNotFound));

            // Update status
            card.setStatus(CardStatus.BLOCKED);
            repository.save(card);

            return new ResponseEntity<>(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            ConstantMessages.cardBlocked,
                            card.getCardNumber()
                    ),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse<String>(HttpStatus.NOT_FOUND.value(), ConstantMessages.cardNotFound,ConstantMessages.properNumber),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError,""),HttpStatus.NOT_FOUND);
        }
    }
    private String generateCardNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
