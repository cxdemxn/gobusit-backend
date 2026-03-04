package com.gobusit.schedule.service;

import com.gobusit.schedule.dto.ScheduleDetailResponse;
import com.gobusit.schedule.dto.ScheduleSummaryResponse;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.schedule.repository.ScheduleRepository;
import com.gobusit.ticket.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;

    public List<ScheduleSummaryResponse> browseSchedules(
            String originName,
            String destinationName,
            LocalDate date) {

        return scheduleRepository
            .browseSchedules(originName, destinationName, date)
            .stream()
            .map(this::toSummary)
            .toList();
    }

    public ScheduleDetailResponse getScheduleDetail(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + scheduleId));

        List<Integer> takenSeats  = ticketRepository.findTakenSeatsByScheduleId(scheduleId);
        int           totalSeats  = schedule.getBus().getCapacity();
        int           available   = totalSeats - takenSeats.size();

        return new ScheduleDetailResponse(
            schedule.getId(),
            schedule.getRoute().getOriginName(),
            schedule.getRoute().getDestinationName(),
            schedule.getBus().getPlateNumber(),
            totalSeats,
            available,
            takenSeats,
            schedule.getDepartureTime(),
            schedule.getArrivalTime(),
            schedule.getPrice(),
            schedule.getStatus()
        );
    }

    // ── Private helpers ──────────────────────────────────────

    private ScheduleSummaryResponse toSummary(Schedule schedule) {
        int totalSeats = schedule.getBus().getCapacity();
        int taken      = ticketRepository.countActiveTicketsByScheduleId(schedule.getId());
        int available  = totalSeats - taken;

        return new ScheduleSummaryResponse(
            schedule.getId(),
            schedule.getRoute().getOriginName(),
            schedule.getRoute().getDestinationName(),
            schedule.getBus().getPlateNumber(),
            totalSeats,
            available,
            schedule.getDepartureTime(),
            schedule.getArrivalTime(),
            schedule.getPrice(),
            schedule.getStatus()
        );
    }
}
