package com.gobusit.ticket.controller;

import com.gobusit.ticket.dto.BookTicketRequest;
import com.gobusit.ticket.dto.PassengerTicketResponse;
import com.gobusit.ticket.service.PassengerTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger/tickets")
@RequiredArgsConstructor
public class PassengerTicketController {

    private final PassengerTicketService ticketService;

    @PostMapping
    public ResponseEntity<PassengerTicketResponse> bookTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid BookTicketRequest req) {
        return ResponseEntity.status(201)
                .body(ticketService.bookTicket(userDetails.getUsername(), req));
    }

    @GetMapping
    public List<PassengerTicketResponse> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ticketService.getMyTickets(userDetails.getUsername());
    }

    @GetMapping("/{id}")
    public PassengerTicketResponse getMyTicketById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {
        return ticketService.getMyTicketById(userDetails.getUsername(), id);
    }

    @PatchMapping("/{id}/cancel")
    public PassengerTicketResponse cancelMyTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {
        return ticketService.cancelMyTicket(userDetails.getUsername(), id);
    }
}
