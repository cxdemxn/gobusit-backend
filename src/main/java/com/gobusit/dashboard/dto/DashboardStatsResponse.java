package com.gobusit.dashboard.dto;

import com.gobusit.dashboard.dto.DashboardScheduleResponse;

import java.util.List;

public record DashboardStatsResponse(
        long totalBuses,
        long totalRoutes,
        long schedulesToday,
        long activeBookings,
        List<DashboardScheduleResponse> todaysSchedules
) {}