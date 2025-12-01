package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationWithResourcesRequest {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long talentId;

    private Long expertId;

    private List<Long> resourceIds;
}
