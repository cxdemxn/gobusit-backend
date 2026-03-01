package com.gobusit.bus.service;

import com.gobusit.bus.dto.BusResponse;
import com.gobusit.bus.dto.CreateBusRequest;
import com.gobusit.bus.dto.UpdateBusRequest;
import com.gobusit.bus.entity.Bus;
import com.gobusit.bus.repository.BusRepository;
import com.gobusit.common.enums.BusStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public BusResponse create(CreateBusRequest req) {
        if (busRepository.existsByPlateNumber(req.plateNumber())) {
            throw new IllegalStateException("Plate number already exists");
        }

        Bus bus = new Bus();
        bus.setPlateNumber(req.plateNumber());
        bus.setCapacity(req.capacity());
        bus.setStatus(req.status() != null ? req.status() : BusStatus.ACTIVE);

        return toResponse(busRepository.save(bus));
    }

    public List<BusResponse> findAll(BusStatus status) {
        List<Bus> buses = (status != null)
                ? busRepository.findByStatus(status)
                : busRepository.findAll();

        return buses.stream().map(this::toResponse).toList();
    }

    public BusResponse findById(String id) {
        return toResponse(getBus(id));
    }

    public BusResponse update(String id, UpdateBusRequest req) {
        Bus bus = getBus(id);
        if (req.plateNumber() != null) bus.setPlateNumber(req.plateNumber());
        if (req.capacity() != null && req.capacity() != 0) bus.setCapacity(req.capacity());
        if (req.status() != null) bus.setStatus(req.status());

        return toResponse(busRepository.save(bus));
    }

    public void delete(String id) {
        busRepository.delete(getBus(id));
    }

// private helpers ──────────────────────────────────────
    private Bus getBus(String id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found: " + id));
    }

    private BusResponse toResponse(Bus bus) {
        return new BusResponse(
                bus.getId(),
                bus.getPlateNumber(),
                bus.getCapacity(),
                bus.getStatus()
        );
    }
}
