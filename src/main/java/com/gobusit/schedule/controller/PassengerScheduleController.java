package com.gobusit.schedule.controller;

import com.gobusit.schedule.dto.ScheduleDetailResponse;
import com.gobusit.schedule.dto.ScheduleSummaryResponse;
import com.gobusit.schedule.service.PassengerScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/passenger/schedules")
@RequiredArgsConstructor
public class PassengerScheduleController {

    private final PassengerScheduleService scheduleService;

    @GetMapping
    public List<ScheduleSummaryResponse> browseSchedules(
            @RequestParam(required = false) String originName,
            @RequestParam(required = false) String destinationName,
            @RequestParam(required = false) String date) {
        return scheduleService.browseSchedules(originName, destinationName, date);
    }

    @GetMapping("/{id}")
    public ScheduleDetailResponse getScheduleDetail(@PathVariable String id) {
        return scheduleService.getScheduleDetail(id);
    }
}
