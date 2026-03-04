package com.gobusit.ticket.dto;

import com.gobusit.common.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponse(
    String       id,
    String       userId,
    String       userEmail,
    String       scheduleId,
    String       originName,
    String       destinationName,
    LocalDateTime departureTime,
    int          seatNumber,
    LocalDateTime bookingTime,
    TicketStatus status
) {}