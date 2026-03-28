package com.gobusit.schedule.controller;

import com.gobusit.common.enums.ScheduleStatus;
import com.gobusit.schedule.dto.CreateScheduleRequest;
import com.gobusit.schedule.dto.ScheduleResponse;
import com.gobusit.schedule.dto.UpdateScheduleRequest;
import com.gobusit.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedules")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleResponse> getAllSchedules(
            @RequestParam(required = false) String         routeId,
            @RequestParam(required = false) ScheduleStatus status,
            @RequestParam(required = false) String date
    ) {
        return scheduleService.findAll(routeId, status, date);
    }

    @GetMapping("/{id}")
    public ScheduleResponse getScheduleById(@PathVariable String id) {
        return scheduleService.findById(id);
    }

    @PutMapping("/{id}")
    public ScheduleResponse updateSchedule(
            @PathVariable String id,
            @RequestBody @Valid UpdateScheduleRequest req) {
        return scheduleService.updateSchedule(id, req);
    }

    @PatchMapping("/{id}/cancel")
    public ScheduleResponse cancelSchedule(@PathVariable String id) {
        return scheduleService.cancelSchedule(id);
    }
}
