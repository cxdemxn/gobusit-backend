package com.gobusit.route.repository;

import com.gobusit.route.entity.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutePointRepository extends JpaRepository<RoutePoint, String> {
    List<RoutePoint> findByRouteIdOrderBySequenceOrderAsc(String routeId);
    boolean existsByRouteIdAndSequenceOrder(String routeId, int sequenceOrder);
    boolean existsByRouteIdAndSequenceOrderAndIdNot(String routeId, int sequenceOrder, String id);
}