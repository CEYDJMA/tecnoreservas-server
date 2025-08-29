package com.farukgenc.boilerplate.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "resources")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String internalId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean availableForBooking;

    @Column(nullable = false)
    private Long servicelineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_line_id", nullable = false)
    private ServiceLine serviceLine;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EquipmentHistory> equipmentHistories;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationResource> reservationResources;
}
