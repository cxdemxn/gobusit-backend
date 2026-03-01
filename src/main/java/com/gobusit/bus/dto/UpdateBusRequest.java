package com.gobusit.bus.dto;

import com.gobusit.common.enums.BusStatus;

public record UpdateBusRequest(
        String plateNumber,
        Integer capacity,
        BusStatus status
) {}
