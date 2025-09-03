package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDto>> getReservations(){
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReservations(@RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity<String> modifyReservation(@Valid @PathVariable Long id, @RequestBody ReservationDto reservationDto){
        System.out.println("la fecha y hora de inicio: " + reservationDto.getDateTimeStart());
        System.out.println("la fecha y hora de fin: " + reservationDto.getEndDateTime());
        return ResponseEntity.ok(reservationService.modification(id, reservationDto));
    }
}
