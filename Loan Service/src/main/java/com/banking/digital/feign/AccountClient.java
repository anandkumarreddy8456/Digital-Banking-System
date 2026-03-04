package com.banking.digital.feign;

import com.banking.digital.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

    @GetMapping("/api/accounts/accountNumber")
    ApiResponse<String> getAccountNumberByMobile(@RequestParam("number") String number);
}
