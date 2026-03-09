package com.banking.digital.feign;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.AccountTransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

    @PutMapping("/api/accounts/withdraw")
    ApiResponse<String> withdraw(@RequestBody AccountTransactionRequest request);

    @PutMapping("/api/accounts/deposit")
    ApiResponse<String> deposit(@RequestBody AccountTransactionRequest request);

    @GetMapping("api/accounts/isValidAccountNumber")
    ApiResponse<String> getAccountNumberByAccountNumber (@RequestParam String accountNumber);
}
