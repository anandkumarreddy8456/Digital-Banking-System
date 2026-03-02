package com.banking.digital.service;

import com.banking.digital.dto.NotificationRequest;
import com.banking.digital.entity.Notification;
import com.banking.digital.entity.NotificationType;
import com.banking.digital.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public String send(NotificationRequest request) {

        Notification notification = new Notification();
        notification.setRecipient(request.getRecipient());
        notification.setMessage(request.getMessage());
        notification.setType(NotificationType.valueOf(request.getType()));
        notification.setStatus("SENT");

        repository.save(notification);

        return "Notification Sent Successfully";
    }
}
