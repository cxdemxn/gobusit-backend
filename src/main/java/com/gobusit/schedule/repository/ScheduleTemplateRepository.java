package com.gobusit.schedule.repository;

import com.gobusit.schedule.entity.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, String> {

    List<ScheduleTemplate> findByActiveTrue();

    // Check if this bus already has an active template overlapping this time window
    // Used to prevent fleet conflicts at template creation time
    @Query("""
        SELECT COUNT(t) > 0 FROM ScheduleTemplate t
        WHERE t.bus.id = :busId
        AND t.active = true
        AND t.id != :excludeId
        AND t.departureTime < :arrivalTime
        AND t.arrivalTime > :departureTime
    """)
    boolean existsOverlappingTemplate(
            @Param("busId")         String busId,
            @Param("departureTime") java.time.LocalTime departureTime,
            @Param("arrivalTime")   java.time.LocalTime arrivalTime,
            @Param("excludeId")     String excludeId
    );
}