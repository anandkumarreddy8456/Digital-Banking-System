package com.banking.digital.controller;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.CardRequest;
import com.banking.digital.dto.CardResponse;
import com.banking.digital.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService service;

    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<CardResponse>> issue(@RequestBody CardRequest request) {

        return service.issueCard(request);
    }

    @PutMapping("/block")
    public ResponseEntity<ApiResponse<String>> block(@RequestParam String cardNumber) {
        return service.blockCard(cardNumber);
    }
}
