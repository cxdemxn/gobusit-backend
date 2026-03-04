package com.gobusit.route.dto;

public record RouteResponse(
    String   uuid,
    String originName,
    String destinationName,
    double distanceKm,
    int    estimatedDurationMin
) {}