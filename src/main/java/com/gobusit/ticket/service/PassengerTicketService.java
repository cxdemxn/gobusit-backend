package com.gobusit.ticket.service;

import com.gobusit.common.enums.ScheduleStatus;
import com.gobusit.common.enums.TicketStatus;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.schedule.repository.ScheduleRepository;
import com.gobusit.ticket.dto.BookTicketRequest;
import com.gobusit.ticket.dto.PassengerTicketResponse;
import com.gobusit.ticket.entity.Ticket;
import com.gobusit.ticket.repository.TicketRepository;
import com.gobusit.user.entity.User;
import com.gobusit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerTicketService {

    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // ── Book ─────────────────────────────────────────────────

    @Transactional
    public PassengerTicketResponse bookTicket(String phoneNumber,
                                              BookTicketRequest req) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Schedule schedule = scheduleRepository.findById(req.scheduleId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Schedule not found: " + req.scheduleId()));

        // Schedule must be bookable
        if (schedule.getStatus() != ScheduleStatus.SCHEDULED &&
            schedule.getStatus() != ScheduleStatus.BOARDING) {
            throw new IllegalStateException(
                "This schedule is not open for booking. Status: " + schedule.getStatus());
        }

        // Seat must be within bus capacity
        int capacity = schedule.getBus().getCapacity();
        if (req.seatNumber() < 1 || req.seatNumber() > capacity) {
            throw new IllegalArgumentException(
                "Seat number must be between 1 and " + capacity);
        }

        // Seat must not already be taken by anyone
        if (ticketRepository.isSeatTaken(req.scheduleId(), req.seatNumber())) {
            throw new IllegalStateException(
                    "Seat " + req.seatNumber() + " is already taken. Please choose another seat.");
        }

        // Passenger can't book twice on the same schedule
        if (ticketRepository.existsByUserIdAndScheduleId(user.getId(), req.scheduleId())) {
            throw new IllegalStateException(
                "You already have a ticket for this schedule");
        }

        // Check no available seats remain
        int taken = ticketRepository.countActiveTicketsByScheduleId(req.scheduleId());
        if (taken >= capacity) {
            throw new IllegalStateException("This schedule is fully booked");
        }

        // Build and save the ticket
        // If two passengers grab the same seat simultaneously,
        // the UNIQUE(schedule_id, seat_number) constraint catches it
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSchedule(schedule);
        ticket.setSeatNumber(req.seatNumber());
        ticket.setBookingTime(LocalDateTime.now());
        ticket.setStatus(TicketStatus.BOOKED);

        try {
            return toResponse(ticketRepository.save(ticket));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                "Seat " + req.seatNumber() + " was just taken. Please choose another seat.");
        }
    }

    // ── View own tickets ─────────────────────────────────────

    public List<PassengerTicketResponse> getMyTickets(String phoneNumber) {
        User user = getUser(phoneNumber);
        return ticketRepository
            .findByUserIdOrderByBookingTimeDesc(user.getId())
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public PassengerTicketResponse getMyTicketById(String phoneNumber, String ticketId) {
        User   user   = getUser(phoneNumber);
        Ticket ticket = getTicket(ticketId);

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                "You do not have permission to view this ticket");
        }

        return toResponse(ticket);
    }

    // ── Cancel own ticket ────────────────────────────────────

    @Transactional
    public PassengerTicketResponse cancelMyTicket(String phoneNumber, String ticketId) {
        User   user   = getUser(phoneNumber);
        Ticket ticket = getTicket(ticketId);

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                "You do not have permission to cancel this ticket");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }
        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Cannot cancel a ticket that has already been used");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        return toResponse(ticketRepository.save(ticket));
    }

    // ── Private helpers ──────────────────────────────────────

    private User getUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Ticket getTicket(String ticketId) {
        return ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + ticketId));
    }

    private PassengerTicketResponse toResponse(Ticket ticket) {
        return new PassengerTicketResponse(
            ticket.getId(),
            ticket.getSchedule().getId(),
            ticket.getSchedule().getRoute().getOriginName(),
            ticket.getSchedule().getRoute().getDestinationName(),
            ticket.getSchedule().getBus().getPlateNumber(),
            ticket.getSchedule().getDepartureTime(),
            ticket.getSchedule().getArrivalTime(),
            ticket.getSchedule().getPrice(),
            ticket.getSeatNumber(),
            ticket.getBookingTime(),
            ticket.getStatus()
        );
    }
}