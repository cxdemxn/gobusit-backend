package com.gobusit.route.repository;

import com.gobusit.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, String> {
    boolean existsByOriginNameAndDestinationName(
        String originName, String destinationName);
}