package com.banking.digital.service;

import com.banking.digital.Common.ApiResponse;
import com.banking.digital.Common.ConstantMessages;
import com.banking.digital.dto.NotificationRequest;
import com.banking.digital.entity.Notification;
import com.banking.digital.entity.NotificationType;
import com.banking.digital.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public ResponseEntity<ApiResponse<String>> send(NotificationRequest request) {

        Notification notification = new Notification();
        notification.setRecipient(request.getRecipient());
        notification.setMessage(request.getMessage());
        notification.setType(NotificationType.valueOf(request.getType()));
        notification.setStatus(ConstantMessages.sentMessage);

        repository.save(notification);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.notificationSent,""),HttpStatus.ACCEPTED);
    }
}
