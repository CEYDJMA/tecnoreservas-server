package com.farukgenc.boilerplate.springboot.model;

import com.farukgenc.boilerplate.springboot.model.enums.ProjectLine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "service_lines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private ProjectLine serviceLineName;

    @OneToMany(mappedBy = "serviceLine", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Expert> experts;

    @OneToMany(mappedBy = "serviceLine", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Resource> resources;
}
