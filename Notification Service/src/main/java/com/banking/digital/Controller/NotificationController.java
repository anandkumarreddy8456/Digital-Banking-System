package com.banking.digital.Controller;

import com.banking.digital.dto.NotificationRequest;
import com.banking.digital.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping("/send")
    public String send(@RequestBody NotificationRequest request) {
        return service.send(request);
    }
}
