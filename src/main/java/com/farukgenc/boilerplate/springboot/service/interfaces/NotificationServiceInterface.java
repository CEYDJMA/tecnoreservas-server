package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationServiceInterface {

    NotificationPageDTO getNotifications(String status, Long userId, Pageable pageable);

}
