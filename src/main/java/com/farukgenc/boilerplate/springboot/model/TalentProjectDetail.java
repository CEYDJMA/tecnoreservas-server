package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TalentProjectDetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String associatedProject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trl_id", nullable = false)
    private TrlOfProject trlOfProject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "talent_id", nullable = false)
    private Talent talent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_line_id", nullable = false)
    private ServiceLine serviceLine;

}
