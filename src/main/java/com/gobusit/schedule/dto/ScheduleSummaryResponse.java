package com.gobusit.schedule.dto;

import com.gobusit.common.enums.ScheduleStatus;

import java.time.LocalDateTime;

// Used in the list view, no seat detail
public record ScheduleSummaryResponse(
    String         id,
    String         originName,
    String         destinationName,
    String         plateNumber,
    int            totalSeats,
    int            availableSeats,
    LocalDateTime departureTime,
    LocalDateTime  arrivalTime,
    double         price,
    ScheduleStatus status
) {}