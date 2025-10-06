package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Talents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Talent extends User {

    @Column(unique = true)
    private String associatedProject;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<DigitalRecord> digitalRecords;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Reservation> reservations;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "talent_project_details", joinColumns = @JoinColumn(name = "talent_id"))
    @Column(name = "project_line", nullable = false)
    private List<Long> projectLines;

}
