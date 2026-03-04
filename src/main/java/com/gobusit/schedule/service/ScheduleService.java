package com.gobusit.schedule.service;

import com.gobusit.bus.entity.Bus;
import com.gobusit.bus.repository.BusRepository;
import com.gobusit.common.enums.BusStatus;
import com.gobusit.common.enums.ScheduleStatus;
import com.gobusit.route.entity.Route;
import com.gobusit.route.repository.RouteRepository;
import com.gobusit.schedule.dto.CreateScheduleRequest;
import com.gobusit.schedule.dto.ScheduleResponse;
import com.gobusit.schedule.dto.UpdateScheduleRequest;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    // ── Create ───────────────────────────────────────────────

    public ScheduleResponse createSchedule(CreateScheduleRequest req) {
        validateTimeWindow(req.departureTime(), req.arrivalTime());

        Bus bus   = getActiveBus(req.busId());
        Route route = getRoute(req.routeId());

        if (scheduleRepository.existsOverlappingSchedule(
                req.busId(), req.departureTime(), req.arrivalTime())) {
            throw new IllegalStateException(
                    "Bus " + bus.getPlateNumber() + " already has a schedule during this time window");
        }

        Schedule schedule = new Schedule();
        schedule.setBus(bus);
        schedule.setRoute(route);
        schedule.setDepartureTime(req.departureTime());
        schedule.setArrivalTime(req.arrivalTime());
        schedule.setPrice(req.price());
        schedule.setStatus(req.status() != null ? req.status() : ScheduleStatus.SCHEDULED);

        return toResponse(scheduleRepository.save(schedule));
    }

    // ── Read ─────────────────────────────────────────────────

    public List<ScheduleResponse> findAll(String routeId, ScheduleStatus status) {
        List<Schedule> results;

        if (routeId != null && status != null) {
            results = scheduleRepository.findByRouteId(routeId)
                    .stream()
                    .filter(s -> s.getStatus() == status)
                    .toList();
        } else if (routeId != null) {
            results = scheduleRepository.findByRouteId(routeId);
        } else if (status != null) {
            results = scheduleRepository.findByStatus(status);
        } else {
            results = scheduleRepository.findAll();
        }

        return results.stream().map(this::toResponse).toList();
    }

    public ScheduleResponse findById(String id) {
        return toResponse(getSchedule(id));
    }

    // ── Update ───────────────────────────────────────────────

    public ScheduleResponse updateSchedule(String id, UpdateScheduleRequest req) {
        Schedule schedule = getSchedule(id);

        if (schedule.getStatus() == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled schedule");
        }

        // Resolve the time window to validate — use existing values if not changing
        LocalDateTime newDeparture = req.departureTime() != null
                ? req.departureTime() : schedule.getDepartureTime();
        LocalDateTime newArrival   = req.arrivalTime() != null
                ? req.arrivalTime() : schedule.getArrivalTime();
        String        newBusId     = req.busId() != null
                ? req.busId() : schedule.getBus().getId();

        validateTimeWindow(newDeparture, newArrival);

        // If bus or time is changing, recheck overlap
        boolean busChanging  = req.busId() != null && !req.busId().equals(schedule.getBus().getId());
        boolean timeChanging = req.departureTime() != null || req.arrivalTime() != null;

        if (busChanging || timeChanging) {
            if (scheduleRepository.existsOverlappingScheduleExcluding(
                    newBusId, newDeparture, newArrival, id)) {
                throw new IllegalStateException(
                        "Bus already has a schedule during this time window");
            }
        }

        if (req.busId()    != null) schedule.setBus(getActiveBus(req.busId()));
        if (req.routeId()  != null) schedule.setRoute(getRoute(req.routeId()));
        if (req.departureTime() != null) schedule.setDepartureTime(req.departureTime());
        if (req.arrivalTime()   != null) schedule.setArrivalTime(req.arrivalTime());
        if (req.price()    != null) schedule.setPrice(req.price());
        if (req.status()   != null) schedule.setStatus(req.status());

        return toResponse(scheduleRepository.save(schedule));
    }

    // ── Cancel ───────────────────────────────────────────────

    public ScheduleResponse cancelSchedule(String id) {
        Schedule schedule = getSchedule(id);

        if (schedule.getStatus() == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Schedule is already cancelled");
        }
        if (schedule.getStatus() == ScheduleStatus.ARRIVED) {
            throw new IllegalStateException("Cannot cancel a schedule that has already arrived");
        }

        schedule.setStatus(ScheduleStatus.CANCELLED);
        return toResponse(scheduleRepository.save(schedule));
    }

    // ── Private helpers ──────────────────────────────────────

    private void validateTimeWindow(LocalDateTime departure, LocalDateTime arrival) {
        if (!departure.isBefore(arrival)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }
    }

    private Bus getActiveBus(String busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found: " + busId));
        if (bus.getStatus() == BusStatus.MAINTENANCE) {
            throw new IllegalStateException(
                    "Bus " + bus.getPlateNumber() + " is currently under maintenance");
        }
        return bus;
    }

    private Route getRoute(String routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + routeId));
    }

    private Schedule getSchedule(String id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + id));
    }

    private ScheduleResponse toResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getRoute().getId(),
                schedule.getRoute().getOriginName(),
                schedule.getRoute().getDestinationName(),
                schedule.getBus().getId(),
                schedule.getBus().getPlateNumber(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getPrice(),
                schedule.getStatus()
        );
    }
}
