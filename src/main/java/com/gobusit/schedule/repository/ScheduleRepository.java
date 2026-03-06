package com.gobusit.schedule.repository;

import com.gobusit.common.enums.ScheduleStatus;
import com.gobusit.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    List<Schedule> findByRouteId(String routeId);
    List<Schedule> findByBusId(String busId);
    List<Schedule> findByStatus(ScheduleStatus status);

    // Overlap check — does this bus have a conflicting schedule?
    @Query("""
        SELECT COUNT(s) > 0 FROM Schedule s
        WHERE s.bus.id = :busId
        AND s.status != 'CANCELLED'
        AND s.departureTime < :arrivalTime
        AND s.arrivalTime > :departureTime
    """)
    boolean existsOverlappingSchedule(
            @Param("busId")        String busId,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("arrivalTime") LocalDateTime arrivalTime
    );

    // Same but exclude a specific schedule (for updates)
    @Query("""
        SELECT COUNT(s) > 0 FROM Schedule s
        WHERE s.bus.id = :busId
        AND s.id != :excludeId
        AND s.status != 'CANCELLED'
        AND s.departureTime < :arrivalTime
        AND s.arrivalTime > :departureTime
    """)
    boolean existsOverlappingScheduleExcluding(
            @Param("busId")        String busId,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("arrivalTime")   LocalDateTime arrivalTime,
            @Param("excludeId")    String excludeId
    );

     // Browse schedules by origin, destination, and date
     @Query(value = """
    SELECT s.id, s.arrival_time, s.bus_id, s.departure_time, s.price, s.route_id, s.status
    FROM schedules s
    JOIN routes r ON r.id = s.route_id
    WHERE (:originName IS NULL OR LOWER(r.origin_name) LIKE LOWER(CONCAT('%', :originName, '%')))
    AND (:destinationName IS NULL OR LOWER(r.destination_name) LIKE LOWER(CONCAT('%', :destinationName, '%')))
    AND (CAST(:date AS date) IS NULL OR DATE(s.departure_time) = CAST(:date AS date))
    AND s.status NOT IN ('CANCELLED', 'ARRIVED')
    ORDER BY s.departure_time ASC
""", nativeQuery = true)
     List<Schedule> browseSchedules(
             @Param("originName")      String originName,
             @Param("destinationName") String destinationName,
             @Param("date")            String date
     );
}
