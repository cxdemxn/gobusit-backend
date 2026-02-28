package com.gobusit.bus.controller;

import com.gobusit.bus.dto.BusResponse;
import com.gobusit.bus.dto.CreateBusRequest;
import com.gobusit.bus.dto.UpdateBusRequest;
import com.gobusit.bus.service.BusService;
import com.gobusit.common.enums.BusStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/buses")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminBusController {

    private final BusService busService;

    @PostMapping
    public ResponseEntity<BusResponse> create(
            @RequestBody
            @Valid
            CreateBusRequest req
) {
        return ResponseEntity.status(201).body(busService.create(req));
    }

    @GetMapping
    public List<BusResponse> getAll(
            @RequestParam(required = false)
            BusStatus status
) {
        return busService.findAll(status);
    }

    @GetMapping("/{id}")
    public BusResponse getById(@PathVariable String id) {
        return busService.findById(id);
    }

    @PutMapping("/{id}")
    public BusResponse update(
            @PathVariable String id,
            @RequestBody
            @Valid
            UpdateBusRequest req
    ) {
        return busService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        busService.delete(id);
        return ResponseEntity.noContent().build();
    }
}