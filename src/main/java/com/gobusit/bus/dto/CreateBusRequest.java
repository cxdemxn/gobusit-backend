package com.gobusit.bus.dto;

import com.gobusit.common.enums.BusStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateBusRequest(
        @NotBlank String plateNumber,
        @Min(1) Integer capacity,
                BusStatus status
) {}
