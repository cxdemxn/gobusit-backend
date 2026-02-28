package com.gobusit.bus.dto;

import com.gobusit.common.enums.BusStatus;

public record BusResponse(
        String uuid,
        String plateNumber,
        Integer capacity,
        BusStatus status
) {}
