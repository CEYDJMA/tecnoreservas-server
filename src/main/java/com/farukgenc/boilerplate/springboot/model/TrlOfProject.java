package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TrlOfProjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrlOfProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String Trl;

    @OneToMany(mappedBy = "trlOfProject", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<TalentProjectDetail> talentProjectDetail;


}
