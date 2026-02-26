package com.gobusit.bus.entity;

import com.gobusit.common.enums.BusStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, name = "plate_number")
    private String plateNumber;

    @Column
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column
    private BusStatus status;
}
