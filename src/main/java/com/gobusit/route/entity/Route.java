package com.gobusit.route.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "destination_name")
    private String destinationName;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "estimated_duration_min")
    private Integer estimatedDurationMin;
}
