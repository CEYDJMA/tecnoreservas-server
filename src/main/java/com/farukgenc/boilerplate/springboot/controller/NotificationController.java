package com.farukgenc.boilerplate.springboot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications() {
        // Simulación de notificaciones en tiempo real
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Notificación en tiempo real: " + sequence);
    }
}
