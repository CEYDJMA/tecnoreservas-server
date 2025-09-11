package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDto>> getReservations(HttpServletRequest request){
        System.out.println("*** LA DIRECCION IP ES: " + request.getRemoteAddr() + " ***");
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDto>> getReservationsStatus(@PathVariable String status) {
        return ResponseEntity.ok(reservationService.getReservationByStatus(status));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationDto>> getReservationsByTalent() {
        return ResponseEntity.ok(reservationService.getReservationByUser());
    }

    @GetMapping("/serviceline/{serviceLine}")
    public ResponseEntity<List<ReservationDto>> getReservationsByServiceLine(@PathVariable ServiceLine serviceLine){
        return ResponseEntity.ok(reservationService.getReservationByServiceLine(serviceLine));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<ReservationDto>> getReservationsByDates(@RequestParam String dateStart, @RequestParam String dateEnd){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(dateStart, formatter);
        LocalDateTime end = LocalDateTime.parse(dateEnd, formatter);

        return ResponseEntity.ok(reservationService.getReservationByDates(start, end));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReservations(@RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity<String> modifyReservation(@Valid @PathVariable Long id, @RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok(reservationService.modification(id, reservationDto));
    }

    @PatchMapping("/canceled/{id}")
    public ResponseEntity<String> canceledReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.canceled(id));
    }

    @PatchMapping("/confirmed/{id}")
    public ResponseEntity<String> confirmedReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.confirmed(id));
    }

    @PatchMapping("/fulfilled/{id}")
    public ResponseEntity<String> fulfilledReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.fulfilled(id));
    }

    @PatchMapping("/missed/{id}")
    public ResponseEntity<String> missedReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.missed(id));
    }


}
