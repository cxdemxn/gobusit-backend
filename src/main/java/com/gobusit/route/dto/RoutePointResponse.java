package com.gobusit.route.dto;

public record RoutePointResponse(
    String   uuid,
    String routeUUID,
    double latitude,
    double longitude,
    int    sequenceOrder,
    String stopName
) {}