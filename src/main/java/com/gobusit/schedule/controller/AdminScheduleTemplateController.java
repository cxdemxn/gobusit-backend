package com.gobusit.schedule.controller;

import com.gobusit.schedule.dto.CreateScheduleTemplateRequest;
import com.gobusit.schedule.dto.ScheduleTemplateResponse;
import com.gobusit.schedule.service.AdminScheduleTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedule-templates")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminScheduleTemplateController {

    private final AdminScheduleTemplateService templateService;

    @PostMapping
    public ResponseEntity<ScheduleTemplateResponse> create(
            @RequestBody @Valid CreateScheduleTemplateRequest req) {
        return ResponseEntity.status(201).body(templateService.create(req));
    }

    @GetMapping
    public List<ScheduleTemplateResponse> getAll() {
        return templateService.findAll();
    }

    @GetMapping("/{id}")
    public ScheduleTemplateResponse getById(@PathVariable String id) {
        return templateService.findById(id);
    }

    @PatchMapping("/{id}/deactivate")
    public ScheduleTemplateResponse deactivate(@PathVariable String id) {
        return templateService.deactivate(id);
    }

    @PatchMapping("/{id}/activate")
    public ScheduleTemplateResponse activate(@PathVariable String id) {
        return templateService.activate(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}