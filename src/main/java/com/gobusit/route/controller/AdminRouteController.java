package com.gobusit.route.controller;

import com.gobusit.route.dto.*;
import com.gobusit.route.service.RoutePointService;
import com.gobusit.route.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/routes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminRouteController {

    private final RouteService routeService;
    private final RoutePointService routePointService;

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(
            @RequestBody @Valid CreateRouteRequest req) {
        return ResponseEntity.status(201).body(routeService.createRoute(req));
    }

    @GetMapping
    public List<RouteResponse> getAllRoutes() {
        return routeService.findAllRoutes();
    }

    @GetMapping("/{id}")
    public RouteResponse getRouteById(@PathVariable String id) {
        return routeService.findRouteById(id);
    }

    @PutMapping("/{id}")
    public RouteResponse updateRoute(@PathVariable String id,
                                     @RequestBody @Valid UpdateRouteRequest req) {
        return routeService.updateRoute(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{routeId}/points")
    public ResponseEntity<RoutePointResponse> addPoint(
            @PathVariable String routeId,
            @RequestBody @Valid CreateRoutePointRequest req) {
        return ResponseEntity.status(201).body(routePointService.addPoint(routeId, req));
    }

    @GetMapping("/{routeId}/points")
    public List<RoutePointResponse> getPoints(@PathVariable String routeId) {
        return routePointService.getPoints(routeId);
    }

    @PutMapping("/{routeId}/points/{pointId}")
    public RoutePointResponse updatePoint(
            @PathVariable String routeId,
            @PathVariable String pointId,
            @RequestBody @Valid UpdateRoutePointRequest req) {
        return routePointService.updatePoint(routeId, pointId, req);
    }

    @DeleteMapping("/{routeId}/points/{pointId}")
    public ResponseEntity<Void> deletePoint(
            @PathVariable String routeId,
            @PathVariable String pointId) {
        routePointService.deletePoint(routeId, pointId);
        return ResponseEntity.noContent().build();
    }
}