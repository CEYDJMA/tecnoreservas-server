package com.farukgenc.boilerplate.springboot.security.dto;

import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private LocalDateTime dateTimeStart;

    private LocalDateTime endDateTime;

    private String reservationStatus;

    private String serviceLine;

    private String expert;

    private String talent;

}
