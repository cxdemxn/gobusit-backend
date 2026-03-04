package com.gobusit.schedule.dto;

import com.gobusit.common.enums.ScheduleStatus;

import java.time.LocalDateTime;

public record UpdateScheduleRequest(
    String        routeId,
    String        busId,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    Double        price,
    ScheduleStatus status
) {}