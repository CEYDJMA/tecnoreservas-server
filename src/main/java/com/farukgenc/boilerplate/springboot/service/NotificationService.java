package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Notification;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationStatus;
import com.farukgenc.boilerplate.springboot.model.enums.NotificationType;
import com.farukgenc.boilerplate.springboot.repository.NotificationRepository;
import com.farukgenc.boilerplate.springboot.repository.ReservationRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final Sinks.Many<NotificationDTO> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

    public NotificationService(NotificationRepository notificationRepository, 
                               UserRepository userRepository,
                               ReservationRepository reservationRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
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

    @Override
    public NotificationDTO createNotification(CreateNotificationRequest request, int flag) {
        // 1. Asignar las entidades relacionadas
        User recipient = request.getTalent();
        User sender = request.getExpert();
        Reservation reservation = request.getReservation();
        
        // 2. Crear la entidad Notification
        Notification notification = new Notification();
        //Notificacion creada del lado del Experto
        if (flag == 0){
            notification.setSenderId(sender.getId());
            notification.setUser(recipient);
            notification.setNotificationType(NotificationType.ACCEPTED);
            notification.setStatus(NotificationStatus.PENDING);
        }
        //Notificacion creada del lado del Talento
        if (flag == 1){
            notification.setSenderId(recipient.getId());
            notification.setUser(sender);
            notification.setNotificationType(NotificationType.NEW_RESERVATION);
            notification.setStatus(NotificationStatus.PENDING);
        }
        notification.setReservation(reservation);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSentAt(LocalDateTime.now());
        
        // 3. Generar y asignar el mensaje personalizado
        String senderName = sender.getName() + " " + sender.getLastname();
        String projectName = reservation.getTalent().getAssociatedProject();
        notification.setMessage(notification.generateMessage(senderName, projectName));
        
        // 4. Guardar en la base de datos
        Notification savedNotification = notificationRepository.save(notification);
        
        // 5. Convertir a DTO
        NotificationDTO notificationDTO = NotificationMapper.mapEntityToDTO(savedNotification);
        
        // 6. Publicar por SSE
        publishNotification(notificationDTO);
        
        // 7. Retornar el DTO
        return notificationDTO;
    }

    // Método auxiliar para publicar nuevas notificaciones (llamar al crear una notificación)
    public void publishNotification(NotificationDTO notificationDTO) {
        notificationSink.tryEmitNext(notificationDTO);
    }
}
