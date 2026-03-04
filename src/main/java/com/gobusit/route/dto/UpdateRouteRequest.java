package com.gobusit.route.dto;

public record UpdateRouteRequest(
    String originName,
    String destinationName,
    Double distanceKm,
    Integer estimatedDurationMin
) {}