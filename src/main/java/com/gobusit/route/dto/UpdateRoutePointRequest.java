package com.gobusit.route.dto;

public record UpdateRoutePointRequest(
    Double latitude,
    Double longitude,
    Integer sequenceOrder,
    String  stopName
) {}