package com.banking.digital.controller;

import com.banking.digital.dto.LoanRequest;
import com.banking.digital.dto.LoanResponse;
import com.banking.digital.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping("/apply")
    public LoanResponse apply(@RequestBody LoanRequest request) {
        return service.apply(request);
    }

    @PutMapping("/approve")
    public LoanResponse approve(@RequestParam Long loanId) {
        return service.approve(loanId);
    }
}
