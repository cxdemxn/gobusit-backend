package com.gobusit.ticket.repository;

import com.gobusit.common.enums.TicketStatus;
import com.gobusit.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByUserId(String userId);
    List<Ticket> findByScheduleId(String scheduleId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByScheduleIdAndStatus(String scheduleId, TicketStatus status);
    List<Ticket> findByUserIdAndStatus(String userId, TicketStatus status);
}