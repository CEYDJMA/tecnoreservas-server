package com.farukgenc.boilerplate.springboot.model;

import com.farukgenc.boilerplate.springboot.model.enums.ProjectLine;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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

    @ElementCollection(targetClass = ProjectLine.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "talent_project_lines", joinColumns = @JoinColumn(name = "talent_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "project_line", nullable = false)
    private List<ProjectLine> projectLines;
}
