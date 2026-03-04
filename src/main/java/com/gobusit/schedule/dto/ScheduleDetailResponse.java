package com.gobusit.schedule.dto;

import com.gobusit.common.enums.ScheduleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduleDetailResponse(
        String         id,
        String         originName,
        String         destinationName,
        String         plateNumber,
        int            totalSeats,
        int            availableSeats,
        List<Integer>  takenSeats,
        LocalDateTime  departureTime,
        LocalDateTime  arrivalTime,
        double         price,
        ScheduleStatus status
) {}