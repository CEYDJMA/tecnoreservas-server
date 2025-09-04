package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Experts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends User {
    
    private Long lineId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_line_id", nullable = false)
    private ServiceLine serviceLine;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DigitalRecord> digitalRecords;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
