package com.gobusit.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalTime;
import java.util.List;

public record CreateScheduleTemplateRequest(
        @NotBlank  String          routeId,
        @NotBlank  String          busId,
        @NotNull   LocalTime       departureTime,
        @NotNull   LocalTime       arrivalTime,
        @Positive  double          price,
        @NotEmpty  List<String>    daysOfWeek
) {}