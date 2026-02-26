package com.gobusit.ticket.entity;

import com.gobusit.common.enums.TicketStatus;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "tickets",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"schedule_id", "seat_number"})
        }
)
public class Ticket {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;
}
