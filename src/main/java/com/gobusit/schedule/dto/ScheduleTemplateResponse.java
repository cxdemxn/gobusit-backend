package com.gobusit.schedule.dto;

import java.time.LocalTime;
import java.util.List;

public record ScheduleTemplateResponse(
        String        id,
        String        routeId,
        String        originName,
        String        destinationName,
        String        busId,
        String        plateNumber,
        LocalTime     departureTime,
        LocalTime     arrivalTime,
        double        price,
        List<String>  daysOfWeek,
        boolean       active
) {}