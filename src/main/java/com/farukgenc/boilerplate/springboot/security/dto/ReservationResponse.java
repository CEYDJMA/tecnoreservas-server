package com.farukgenc.boilerplate.springboot.security.dto;

import com.farukgenc.boilerplate.springboot.model.enums.ReservationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservationResponse {

    private LocalDateTime dateTimeStart;

    private LocalDateTime endDateTime;

    private ReservationStatus status;

    private String serviceLine;

    private String expert;

    private String talent;

}
