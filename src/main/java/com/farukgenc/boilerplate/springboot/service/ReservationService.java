package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Expert;
import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.Talent;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.ReservationRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public String createReservation(ReservationDto reservationDto) {
        //Validacion del experto y talento
        Long expertId = reservationDto.getExpert();
        Optional<Expert> expert = expertRepository.findById(expertId);
        Long talentId = reservationDto.getTalent();
        Optional<Talent> talent = talentRepository.findById(talentId);
        //obtencion de datos
        Reservation newReservation = new Reservation();
        newReservation.setDateTimeStart(reservationDto.getDateTimeStart());
        newReservation.setEndDateTime(reservationDto.getEndDateTime());
        newReservation.setReservationStatus("solicitado");
        newReservation.setCreationDate(Date.from(Instant.now()));
        newReservation.setLastModifiedDate(Date.from(Instant.now()));
        newReservation.setExpert(expert.get());
        newReservation.setTalent(talent.get());
        reservationRepository.save(newReservation);
        Optional<User> userTalento = userRepository.findById(talentId);
        return userTalento.get().getName() + " ha agendado con exito";
    }

    public String modification(Long id, ReservationDto reservationDto) {
        Long expertId = reservationDto.getExpert();
        Optional<Expert> expert = expertRepository.findById(expertId);
        Long talentId = reservationDto.getTalent();
        Optional<Talent> talent = talentRepository.findById(talentId);
        Reservation reservation = reservationRepository.findById(id).orElseThrow();

        expert.ifPresent(reservation::setExpert);
        talent.ifPresent(reservation::setTalent);

        if (reservationDto.getDateTimeStart() != null){
            reservation.setDateTimeStart(reservationDto.getDateTimeStart());
        }
        if (reservationDto.getEndDateTime() != null){
            reservation.setEndDateTime(reservationDto.getEndDateTime());
        }
        reservationRepository.save(reservation);
        return "la reserva de " + reservation.getTalent().getName() + " ha sido modificada.";
    }
}
