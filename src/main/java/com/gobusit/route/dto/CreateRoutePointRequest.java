package com.gobusit.route.dto;

import jakarta.validation.constraints.NotNull;

public record CreateRoutePointRequest(
    @NotNull double latitude,
    @NotNull double longitude,
    @NotNull int    sequenceOrder,
             String stopName
) {}