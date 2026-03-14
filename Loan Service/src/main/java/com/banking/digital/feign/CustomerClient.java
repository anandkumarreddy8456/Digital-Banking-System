package com.banking.digital.feign;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {

    @GetMapping("/api/customers/getById")
    ApiResponse<CustomerResponse> getCustomerById(@RequestParam("id") Long id);
}
