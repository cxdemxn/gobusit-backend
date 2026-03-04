package com.gobusit.ticket.service;

import com.gobusit.common.enums.TicketStatus;
import com.gobusit.ticket.dto.TicketResponse;
import com.gobusit.ticket.entity.Ticket;
import com.gobusit.ticket.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTicketService {

    private final TicketRepository ticketRepository;

    public List<TicketResponse> findAll(String scheduleId,
                                        String userId,
                                        TicketStatus status) {
        List<Ticket> results;

        if (scheduleId != null && status != null) {
            results = ticketRepository.findByScheduleIdAndStatus(scheduleId, status);
        } else if (userId != null && status != null) {
            results = ticketRepository.findByUserIdAndStatus(userId, status);
        } else if (scheduleId != null) {
            results = ticketRepository.findByScheduleId(scheduleId);
        } else if (userId != null) {
            results = ticketRepository.findByUserId(userId);
        } else if (status != null) {
            results = ticketRepository.findByStatus(status);
        } else {
            results = ticketRepository.findAll();
        }

        return results.stream().map(this::toResponse).toList();
    }

    public TicketResponse findById(String id) {
        return toResponse(getTicket(id));
    }

    public TicketResponse cancelTicket(String id) {
        Ticket ticket = getTicket(id);

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }
        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Cannot cancel a ticket that has already been used");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        return toResponse(ticketRepository.save(ticket));
    }


    private Ticket getTicket(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + id));
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getUser().getId(),
                ticket.getUser().getEmail(),
                ticket.getSchedule().getId(),
                ticket.getSchedule().getRoute().getOriginName(),
                ticket.getSchedule().getRoute().getDestinationName(),
                ticket.getSchedule().getDepartureTime(),
                ticket.getSeatNumber(),
                ticket.getBookingTime(),
                ticket.getStatus()
        );
    }
}
