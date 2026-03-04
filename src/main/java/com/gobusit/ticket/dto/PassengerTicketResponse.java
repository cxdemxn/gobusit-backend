package com.gobusit.ticket.dto;

import com.gobusit.common.enums.TicketStatus;

import java.time.LocalDateTime;

public record PassengerTicketResponse(
    String        id,
    String        scheduleId,
    String        originName,
    String        destinationName,
    String        plateNumber,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    double        price,
    int           seatNumber,
    LocalDateTime bookingTime,
    TicketStatus status
) {}