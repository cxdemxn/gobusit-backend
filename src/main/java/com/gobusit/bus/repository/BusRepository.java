package com.gobusit.bus.repository;

import com.gobusit.bus.entity.Bus;
import com.gobusit.common.enums.BusStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, String> {
    List<Bus> findByStatus(BusStatus busStatus);
    boolean existsByPlateNumber(String plateNumber);
}
