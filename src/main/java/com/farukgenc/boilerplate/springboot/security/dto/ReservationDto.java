package com.farukgenc.boilerplate.springboot.security.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReservationDto {

    @FutureOrPresent(message = "La fecha de inicio no puede ser menor a la actual")
    private LocalDateTime dateTimeStart;
    @FutureOrPresent(message = "La fecha de fin no puede ser menor a la actual")
    private LocalDateTime endDateTime;

    private Long expert;

    private Long talent;

}
