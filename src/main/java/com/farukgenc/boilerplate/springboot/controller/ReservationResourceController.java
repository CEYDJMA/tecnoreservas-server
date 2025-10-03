package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.service.ReservationResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation/resources")
public class ReservationResourceController {

    @Autowired
    private ReservationResourceService reservationResourceService;

    @PostMapping("/assign/{reserve}")
    public ResponseEntity<String> assignResourceToReserve(@RequestBody List<Long> resources, @PathVariable Long reserve){
        return ResponseEntity.ok().body(reservationResourceService.assignResource(resources, reserve));
    }
}