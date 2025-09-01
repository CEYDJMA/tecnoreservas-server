package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDto {

    private Date dateTimeStart;

    private Date endDateTime;

    private Long expert;

    private Long talent;

}
