package com.gobusit.dashboard.service;

import com.gobusit.bus.repository.BusRepository;
import com.gobusit.common.enums.TicketStatus;
import com.gobusit.dashboard.dto.DashboardScheduleResponse;
import com.gobusit.dashboard.dto.DashboardStatsResponse;
import com.gobusit.route.repository.RouteRepository;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.schedule.repository.ScheduleRepository;
import com.gobusit.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;

    public DashboardStatsResponse getStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        long totalBuses = busRepository.count();
        long totalRoutes = routeRepository.count();

        List<Schedule> todaysSchedules = scheduleRepository.findSchedulesToday(startOfDay, endOfDay);

        long schedulesToday = todaysSchedules.size();
        long activeBookings = ticketRepository.countByStatus(TicketStatus.BOOKED);

        List<DashboardScheduleResponse> scheduleResponses = todaysSchedules.stream()
                .map(s -> new DashboardScheduleResponse(
                        s.getId(),
                        s.getRoute().getOriginName(),
                        s.getRoute().getDestinationName(),
                        s.getDepartureTime().toString(),
                        s.getBus().getPlateNumber(),
                        s.getStatus(),
                        ticketRepository.countActiveTicketsByScheduleId(s.getId()),
                        s.getBus().getCapacity()
                ))
                .toList();

        return new DashboardStatsResponse(
                totalBuses,
                totalRoutes,
                schedulesToday,
                activeBookings,
                scheduleResponses
        );
    }
}