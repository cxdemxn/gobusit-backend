package com.gobusit.route.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateRouteRequest(
    @NotBlank String originName,
    @NotBlank String destinationName,
    @Positive double distanceKm,
    @Positive int estimatedDurationMin
) {}