package com.banking.digital.feign;

import com.banking.digital.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Map;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/api/notifications/send")
    ApiResponse<String> sendNotification(@RequestBody Map<String, String> request);

}
