package com.banking.digital.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

    @PutMapping("/api/accounts/withdraw")
    String withdraw(@RequestBody Object request);

    @PutMapping("/api/accounts/deposit")
    String deposit(@RequestBody Object request);
}
