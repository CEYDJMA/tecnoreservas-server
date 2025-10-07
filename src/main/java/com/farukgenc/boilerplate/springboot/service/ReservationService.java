package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.ReservationRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.security.dto.notification.CreateNotificationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.mapper.notifications.NotificationMapper;
import com.farukgenc.boilerplate.springboot.security.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private NotificationService notificationService;

    public List<ReservationDto> getReservations() {
        List<Reservation> listaReservas = reservationRepository.findAll();
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listaReservas){
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            response.add(reservationDto);
        }
        return response;
    }

    public List<ReservationDto> getReservationByStatus(String status){
        List<Reservation> listStatus = reservationRepository.findAllByReservationStatus(status);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listStatus) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            response.add(reservationDto);
        }
        return response;
    }

    public List<ReservationDto> getReservationByUser(){
        //Obtener el usuario logueado
        String username = userServiceImpl.getLoggedUser();
        //BUscar el usuario y guardarlo en variable
        User user = userRepository.findByUsername(username);
        List<ReservationDto> response = new ArrayList<>();
        if (user.getUserRole() == UserRole.EXPERT) {
            Expert expert = expertRepository.findById(user.getId()).orElseThrow();
            List<Reservation> listado = reservationRepository.findAllByExpert_Id(user.getId());
            for (Reservation reservation : listado) {
                    ReservationDto reservationDto = new ReservationDto();
                    reservationDto.setDateTimeStart(reservation.getDateTimeStart());
                    reservationDto.setEndDateTime(reservation.getEndDateTime());
                    reservationDto.setExpert(reservation.getExpert().getId());
                    reservationDto.setTalent(reservation.getTalent().getId());
                    response.add(reservationDto);
                }
                return response;
        } else if (user.getUserRole() == UserRole.TALENT) {
            Talent talent = talentRepository.findById(user.getId()).orElseThrow();
            List<Reservation> listado = reservationRepository.findAllByTalent_Id(user.getId());
            for (Reservation reservation : listado) {
                ReservationDto reservationDto = new ReservationDto();
                reservationDto.setDateTimeStart(reservation.getDateTimeStart());
                reservationDto.setEndDateTime(reservation.getEndDateTime());
                reservationDto.setExpert(reservation.getExpert().getId());
                reservationDto.setTalent(reservation.getTalent().getId());
                response.add(reservationDto);
            }
        }
            return response;
    }

    public List<ReservationDto> getReservationByServiceLine(ServiceLine serviceLine){
        List<Reservation> listServiceLine = reservationRepository.findAllByExpert_ServiceLine(serviceLine);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation: listServiceLine) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            response.add(reservationDto);
        }
        return response;
    }

    public List<ReservationDto> getReservationByDates(LocalDateTime date1, LocalDateTime date2) {
        List<Reservation> listByDates = reservationRepository.findByDateTimeStartBetween(date1, date2);
        List<ReservationDto> response = new ArrayList<>();
        for (Reservation reservation : listByDates) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setDateTimeStart(reservation.getDateTimeStart());
            reservationDto.setEndDateTime(reservation.getEndDateTime());
            reservationDto.setExpert(reservation.getExpert().getId());
            reservationDto.setTalent(reservation.getTalent().getId());
            response.add(reservationDto);
        }
        return response;
    }

    @Transactional
    public String createReservation(ReservationDto reservationDto) {
        //Validacion del experto y talento
        Long expertId = reservationDto.getExpert();
        Optional<Expert> expert = expertRepository.findById(expertId);
        Long talentId = reservationDto.getTalent();
        Optional<Talent> talent = talentRepository.findById(talentId);
        //asignacion de tiempo
        LocalDateTime start = reservationDto.getDateTimeStart();
        LocalDateTime end = reservationDto.getEndDateTime();
        LocalTime time = start
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        // horario no disponible
        LocalTime almuerzoInicio = LocalTime.of(12,0);
        LocalTime almuerzoFin = LocalTime.of(14,0);
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        LocalDate fecha1 = start.toLocalDate();
        LocalDate fecha2 = end.toLocalDate();
        LocalTime inicio = LocalTime.of(8,0);
        LocalTime fin = LocalTime.of(16,0);
        boolean intersectaConAlmuerzo = !endTime.isBefore(almuerzoInicio) && !startTime.isAfter(almuerzoFin);

        if (time.isBefore(inicio) || time.isAfter(fin)) {
            throw new IllegalArgumentException("solo se permiten reservas entre las 8:00 y 16:00 horas.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("El tiempo de fin de la reserva no puede ser menor al tiempo de inicio de la reserva.");
        }
        if (end.getHour() - start.getHour() < 1) {
            throw new IllegalArgumentException("El tiempo de reserva no puede ser inferior a una hora.");
        }
        if (!fecha1.equals(fecha2)) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la reserva deben ser en el mismo día.");
        }
        if (intersectaConAlmuerzo) {
            throw new IllegalArgumentException("No se puede reservar entre las 12:01 y las 13:59.");
        }
        //obtencion de datos
        Reservation newReservation = new Reservation();
        newReservation.setDateTimeStart(reservationDto.getDateTimeStart());
        newReservation.setEndDateTime(reservationDto.getEndDateTime());
        newReservation.setReservationStatus(ReservationStatus.SOLICITADA);
        newReservation.setCreationDate(LocalDateTime.now());
        newReservation.setLastModifiedDate(LocalDateTime.now());
        newReservation.setExpert(expert.get());
        newReservation.setTalent(talent.get());
        // Crear nueva notificacion
        Reservation reservationid = reservationRepository.save(newReservation);
        CreateNotificationRequest notificationRequestDto =
                NotificationMapper.buildCreateNotificationRequest(talentId,expertId,reservationid.getId());
        NotificationDTO notificationDTO = notificationService.createNotification(notificationRequestDto);
        Optional<User> userTalento = userRepository.findById(talentId);
        return " Reserva agendada con exito";
    }

    public String modification(Long id, ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        //valida que la fecha no venga vacía
        LocalDateTime start = reservationDto.getDateTimeStart();
        LocalDateTime end = reservationDto.getEndDateTime();
        LocalTime time = start
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        //limitacion de horas
        LocalTime inicio = LocalTime.of(8,0);
        LocalTime almuerzoInicio = LocalTime.of(12, 1);
        LocalTime almuerzoFin = LocalTime.of(13,59);
        LocalTime fin = LocalTime.of(16,0);
        LocalDate fecha1 = start.toLocalDate();
        LocalDate fecha2 = end.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        boolean intersectaConAlmuerzo = !endTime.isBefore(almuerzoInicio) && !startTime.isAfter(almuerzoFin);

        if (time.isBefore(inicio) || time.isAfter(fin)) {
            throw new IllegalArgumentException("solo se permiten reservas entre las 8:00 y 16:00 horas.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("El tiempo de fin de la reserva no puede ser menor al tiempo de inicio de la reserva.");
        }
        if (end.getHour() - start.getHour() < 1) {
            throw new IllegalArgumentException("El tiempo de reserva no puede ser inferior a una hora.");
        }
        if (!fecha1.equals(fecha2)) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la reserva deben ser en el mismo día.");
        }
        if (intersectaConAlmuerzo) {
            throw new IllegalArgumentException("No se puede reservar entre las 12:01 y las 13:59.");
        }

        reservation.setDateTimeStart(reservationDto.getDateTimeStart());
        reservation.setEndDateTime(reservationDto.getEndDateTime());
        reservation.setLastModifiedDate(LocalDateTime.now());

        reservationRepository.save(reservation);
        return "la reserva de " + reservation.getTalent().getName() + " ha sido modificada.";
    }

    @Transactional
    public String canceled(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CANCELADA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido cancelada.";
    }

    @Transactional
    public String confirmed(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CONFIRMADA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido confirmada.";
    }

    @Transactional
    public String fulfilled(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.CUMPLIDA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido cumplida.";
    }

    @Transactional
    public String missed(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservation.setReservationStatus(ReservationStatus.INCUMPLIDA);
        reservationRepository.save(reservation);

        return "La reserva de " + reservation.getTalent().getName() + " ha sido incumplida.";
    }
}
