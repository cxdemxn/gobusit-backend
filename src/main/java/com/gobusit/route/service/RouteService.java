package com.gobusit.route.service;

import com.gobusit.route.dto.*;
import com.gobusit.route.entity.Route;
import com.gobusit.route.entity.RoutePoint;
import com.gobusit.route.repository.RoutePointRepository;
import com.gobusit.route.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    // ── Route CRUD ───────────────────────────────────────────

    public RouteResponse createRoute(CreateRouteRequest req) {
        if (routeRepository.existsByOriginNameAndDestinationName(
                req.originName(), req.destinationName())) {
            throw new IllegalStateException(
                    "Route from " + req.originName() + " to " + req.destinationName() + " already exists");
        }
        Route route = new Route();
        route.setOriginName(req.originName());
        route.setDestinationName(req.destinationName());
        route.setDistanceKm(req.distanceKm());
        route.setEstimatedDurationMin(req.estimatedDurationMin());
        return toRouteResponse(routeRepository.save(route));
    }

    public List<RouteResponse> findAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::toRouteResponse)
                .toList();
    }

    public RouteResponse findRouteById(String id) {
        return toRouteResponse(getRoute(id));
    }

    public RouteResponse updateRoute(String id, UpdateRouteRequest req) {
        Route route = getRoute(id);
        if (req.originName()           != null) route.setOriginName(req.originName());
        if (req.destinationName()      != null) route.setDestinationName(req.destinationName());
        if (req.distanceKm()           != null) route.setDistanceKm(req.distanceKm());
        if (req.estimatedDurationMin() != null) route.setEstimatedDurationMin(req.estimatedDurationMin());
        return toRouteResponse(routeRepository.save(route));
    }

    public void deleteRoute(String id) {
        routeRepository.delete(getRoute(id));
    }

    // ── RoutePoint CRUD ──────────────────────────────────────



    // ── private helpers ──────────────────────────────────────

    protected Route getRoute(String id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + id));
    }

    private RouteResponse toRouteResponse(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getOriginName(),
                route.getDestinationName(),
                route.getDistanceKm(),
                route.getEstimatedDurationMin()
        );
    }
}
