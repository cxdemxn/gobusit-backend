package com.gobusit.ticket.repository;

import com.gobusit.common.enums.TicketStatus;
import com.gobusit.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByUserId(String userId);
    List<Ticket> findByScheduleId(String scheduleId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByScheduleIdAndStatus(String scheduleId, TicketStatus status);
    List<Ticket> findByUserIdAndStatus(String userId, TicketStatus status);

    // Excludes cancelled seats
    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.status != 'CANCELLED'")
    List<Integer> findTakenSeatsByScheduleId(@Param("scheduleId") String scheduleId);

    // Count of active tickets on a schedule
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.status != 'CANCELLED'")
    int countActiveTicketsByScheduleId(@Param("scheduleId") String scheduleId);

    // Check if a specific seat is taken
    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.seatNumber = :seatNumber AND t.status != 'CANCELLED'")
    boolean isSeatTaken(@Param("scheduleId") String scheduleId, @Param("seatNumber") int seatNumber);

    // Passenger's own tickets
    List<Ticket> findByUserIdOrderByBookingTimeDesc(String userId);

    // Check if passenger already has a ticket on this schedule
    boolean existsByUserIdAndScheduleId(String userId, String scheduleId);
}