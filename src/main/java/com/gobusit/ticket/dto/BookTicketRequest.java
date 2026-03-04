package com.gobusit.ticket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookTicketRequest(
    @NotBlank String scheduleId,
    @NotNull @Min(1) Integer seatNumber
) {}