package com.gobusit.route.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
