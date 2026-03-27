package com.gobusit.schedule.service;

import com.gobusit.bus.entity.Bus;
import com.gobusit.bus.repository.BusRepository;
import com.gobusit.common.enums.BusStatus;
import com.gobusit.route.entity.Route;
import com.gobusit.route.repository.RouteRepository;
import com.gobusit.schedule.dto.CreateScheduleTemplateRequest;
import com.gobusit.schedule.dto.ScheduleTemplateResponse;
import com.gobusit.schedule.entity.ScheduleTemplate;
import com.gobusit.schedule.repository.ScheduleTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminScheduleTemplateService {

    private final ScheduleTemplateRepository templateRepository;
    private final TripGenerationService tripGenerationService;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    public ScheduleTemplateResponse create(CreateScheduleTemplateRequest req) {
        Bus bus = getActiveBus(req.busId());
        Route route = getRoute(req.routeId());

        if (!req.departureTime().isBefore(req.arrivalTime())) {
            throw new IllegalArgumentException(
                    "Departure time must be before arrival time");
        }

        // check for fleet conflicts with other active templates
        boolean conflict = templateRepository.existsOverlappingTemplate(
                req.busId(),
                req.departureTime(),
                req.arrivalTime(),
                "NONE" // no template to exclude on creation
        );
        if (conflict) {
            throw new IllegalStateException(
                    "Bus " + bus.getPlateNumber() +
                            " already has an active template during this time window");
        }

        ScheduleTemplate template = new ScheduleTemplate();
        template.setRoute(route);
        template.setBus(bus);
        template.setDepartureTime(req.departureTime());
        template.setArrivalTime(req.arrivalTime());
        template.setPrice(req.price());
        template.setDaysOfWeek(String.join(",", req.daysOfWeek()));
        template.setActive(true);

        ScheduleTemplate saved = templateRepository.save(template);

        // generate first 14 days of trips immediately
        tripGenerationService.generateTripsForTemplate(saved);

        return toResponse(saved);
    }

    public List<ScheduleTemplateResponse> findAll() {
        return templateRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ScheduleTemplateResponse findById(String id) {
        return toResponse(getTemplate(id));
    }

    public ScheduleTemplateResponse deactivate(String id) {
        ScheduleTemplate template = getTemplate(id);
        if (!template.isActive()) {
            throw new IllegalStateException("Template is already inactive");
        }
        template.setActive(false);
        return toResponse(templateRepository.save(template));
    }

    public ScheduleTemplateResponse activate(String id) {
        ScheduleTemplate template = getTemplate(id);
        if (template.isActive()) {
            throw new IllegalStateException("Template is already active");
        }
        template.setActive(true);
        // generate trips going forward immediately on reactivation
        tripGenerationService.generateTripsForTemplate(template);
        return toResponse(templateRepository.save(template));
    }

    public void delete(String id) {
        templateRepository.delete(getTemplate(id));
    }

    // --- private helpers -------------------------------------

    private ScheduleTemplate getTemplate(String id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Schedule template not found: " + id));
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Route not found: " + routeId));
    }

    private ScheduleTemplateResponse toResponse(ScheduleTemplate t) {
        return new ScheduleTemplateResponse(
                t.getId(),
                t.getRoute().getId(),
                t.getRoute().getOriginName(),
                t.getRoute().getDestinationName(),
                t.getBus().getId(),
                t.getBus().getPlateNumber(),
                t.getDepartureTime(),
                t.getArrivalTime(),
                t.getPrice(),
                List.of(t.getDaysOfWeek().split(",")),
                t.isActive()
        );
    }
}