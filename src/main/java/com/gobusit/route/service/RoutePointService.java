package com.gobusit.route.service;

import com.gobusit.route.dto.CreateRoutePointRequest;
import com.gobusit.route.dto.RoutePointResponse;
import com.gobusit.route.dto.UpdateRoutePointRequest;
import com.gobusit.route.entity.Route;
import com.gobusit.route.entity.RoutePoint;
import com.gobusit.route.repository.RoutePointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutePointService {

    private final RoutePointRepository routePointRepository;
    private final RouteService routeService;

    public RoutePointResponse addPoint(String routeId, CreateRoutePointRequest req) {

        Route route = routeService.getRoute(routeId);

        if (routePointRepository.existsByRouteIdAndSequenceOrder(
                routeId, req.sequenceOrder())) {
            throw new IllegalStateException(
                    "Sequence order " + req.sequenceOrder() + " already exists on this route");
        }

        RoutePoint point = new RoutePoint();
        point.setRoute(route);
        point.setLatitude(req.latitude());
        point.setLongitude(req.longitude());
        point.setSequenceOrder(req.sequenceOrder());
        point.setStopName(req.stopName());

        return toPointResponse(routePointRepository.save(point));
    }

    public List<RoutePointResponse> getPoints(String routeId) {
        routeService.getRoute(routeId); // verify route exists
        return routePointRepository
                .findByRouteIdOrderBySequenceOrderAsc(routeId)
                .stream()
                .map(this::toPointResponse)
                .toList();
    }

    public RoutePointResponse updatePoint(String routeId, String pointId,
                                          UpdateRoutePointRequest req) {
        routeService.getRoute(routeId); // verify route exists
        RoutePoint point = getPoint(pointId, routeId);

        if (req.sequenceOrder() != null) {
            boolean duplicate = routePointRepository
                    .existsByRouteIdAndSequenceOrderAndIdNot(
                            routeId, req.sequenceOrder(), pointId);
            if (duplicate) {
                throw new IllegalStateException(
                        "Sequence order " + req.sequenceOrder() + " already exists on this route");
            }
            point.setSequenceOrder(req.sequenceOrder());
        }

        if (req.latitude()  != null) point.setLatitude(req.latitude());
        if (req.longitude() != null) point.setLongitude(req.longitude());
        if (req.stopName()  != null) point.setStopName(req.stopName());

        return toPointResponse(routePointRepository.save(point));
    }

    public void deletePoint(String routeId, String pointId) {
        routeService.getRoute(routeId); // verify route exists
        routePointRepository.delete(getPoint(pointId, routeId));
    }

    private RoutePoint getPoint(String pointId, String routeId) {
        RoutePoint point = routePointRepository.findById(pointId)
                .orElseThrow(() -> new EntityNotFoundException("Route point not found: " + pointId));
        if (!point.getRoute().getId().equals(routeId)) {
            throw new EntityNotFoundException(
                    "Route point " + pointId + " does not belong to route " + routeId);
        }
        return point;
    }

    private RoutePointResponse toPointResponse(RoutePoint point) {
        return new RoutePointResponse(
                point.getId(),
                point.getRoute().getId(),
                point.getLatitude(),
                point.getLongitude(),
                point.getSequenceOrder(),
                point.getStopName()
        );
    }
}
