package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.repository.NotificationRepository;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import com.farukgenc.boilerplate.springboot.security.mapper.notifications.NotificationMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.NotificationServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;
    private final Sinks.Many<NotificationDTO> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

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

    @Override
    public Flux<NotificationDTO> streamNotifications(Long userId) {
        // 1. Emitir notificaciones pendientes/históricas al conectar
        List<NotificationDTO> pending = notificationRepository.findAll().stream()
            .filter(n -> n.getUser().getId().equals(userId))
            .filter(n -> n.getStatus().name().equalsIgnoreCase("PENDING"))
            .map(NotificationMapper::mapEntityToDTO)
            .toList();
        Flux<NotificationDTO> pendingFlux = Flux.fromIterable(pending);

        // 2. Emitir nuevas notificaciones en tiempo real
        Flux<NotificationDTO> realtimeFlux = notificationSink.asFlux()
            .filter(dto -> dto.getUserId().equals(userId));

        // 3. Combinar ambos flujos
        return Flux.concat(pendingFlux, realtimeFlux);
    }

    // Método auxiliar para publicar nuevas notificaciones (llamar al crear una notificación)
    public void publishNotification(NotificationDTO notificationDTO) {
        notificationSink.tryEmitNext(notificationDTO);
    }
}
