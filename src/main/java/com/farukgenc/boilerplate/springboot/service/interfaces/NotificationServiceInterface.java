package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface NotificationServiceInterface {

    NotificationPageDTO getNotifications(String status, Long userId, Pageable pageable);

    Flux<NotificationDTO> streamNotifications(Long userId);

}
