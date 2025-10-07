package com.farukgenc.boilerplate.springboot.security.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequest {

    private Long senderId;
    private Long userId;
    private Long reservationId;
}
