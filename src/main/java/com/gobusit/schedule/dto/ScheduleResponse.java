package com.gobusit.schedule.dto;

import com.gobusit.common.enums.ScheduleStatus;

import java.time.LocalDateTime;

public record ScheduleResponse(
    String         id,
    String         routeId,
    String         originName,
    String         destinationName,
    String         busId,
    String         plateNumber,
    LocalDateTime departureTime,
    LocalDateTime  arrivalTime,
    double         price,
    ScheduleStatus status
) {}