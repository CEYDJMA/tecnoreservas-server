package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.repository.NotificationRepository;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import com.farukgenc.boilerplate.springboot.security.mapper.notifications.NotificationMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.NotificationServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;


    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationPageDTO getNotifications(String status, Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);

        // Convert Page to a List for filtering
        List<Notification> filteredNotifications = notifications.getContent().stream()
            .filter(notification -> status == null || notification.getStatus().name().equalsIgnoreCase(status))
            .filter(notification -> userId == null || notification.getUser().getId().equals(userId))
            .toList();

        // Map the filtered list back to a Page
        Page<Notification> filteredPage = new PageImpl<>(filteredNotifications, pageable, filteredNotifications.size());

        return NotificationMapper.mapPageToPagedResponse(filteredPage);
    }
}
