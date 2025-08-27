package com.farukgenc.boilerplate.springboot.model;

import com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "resources")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "resource_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false, unique = true)
    private String internalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_line_id", nullable = false)
    private ServiceLine serviceLine;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EquipmentHistory> equipmentHistories;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationResource> reservationResources;
}
