package com.gobusit.dashboard.dto;

import com.gobusit.common.enums.ScheduleStatus;

public record DashboardScheduleResponse(
        String id,
        String originName,
        String destinationName,
        String departureTime,
        String plateNumber,
        ScheduleStatus status,
        int bookedSeats,
        int totalSeats
) {}