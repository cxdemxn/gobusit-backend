package com.gobusit.schedule.dto;

import com.gobusit.common.enums.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateScheduleRequest(
    @NotBlank String        routeId,
    @NotBlank  String        busId,
    @NotNull LocalDateTime departureTime,
    @NotNull   LocalDateTime arrivalTime,
    @Positive double        price,
               ScheduleStatus status
) {}