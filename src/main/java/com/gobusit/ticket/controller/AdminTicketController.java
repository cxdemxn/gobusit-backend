package com.gobusit.ticket.controller;

import com.gobusit.common.enums.TicketStatus;
import com.gobusit.ticket.dto.TicketResponse;
import com.gobusit.ticket.service.AdminTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tickets")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminTicketController {

    private final AdminTicketService adminTicketService;

    @GetMapping
    public List<TicketResponse> getAllTickets(
            @RequestParam(required = false) String       scheduleId,
            @RequestParam(required = false) String       userId,
            @RequestParam(required = false) TicketStatus status) {
        return adminTicketService.findAll(scheduleId, userId, status);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable String id) {
        return adminTicketService.findById(id);
    }

    @PatchMapping("/{id}/cancel")
    public TicketResponse cancelTicket(@PathVariable String id) {
        return adminTicketService.cancelTicket(id);
    }
}
