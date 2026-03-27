package com.gobusit.schedule.service;

import com.gobusit.common.enums.ScheduleStatus;
import com.gobusit.schedule.entity.Schedule;
import com.gobusit.schedule.entity.ScheduleTemplate;
import com.gobusit.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripGenerationService {

    private static final int DAYS_AHEAD = 14;

    private final ScheduleRepository scheduleRepository;

    // called when a template is first created
    public void generateTripsForTemplate(ScheduleTemplate template) {
        LocalDate today = LocalDate.now();
        generateForRange(template, today, today.plusDays(DAYS_AHEAD));
    }

    // called by the nightly job for all active templates
    public void extendTripsForTemplate(ScheduleTemplate template) {
        LocalDate today = LocalDate.now();
        LocalDate target = today.plusDays(DAYS_AHEAD);

        LocalDateTime latestDeparture =
                scheduleRepository.findLatestDepartureDateByTemplateId(template.getId());

        // start generating from the day after the last existing trip
        // ff no trips exist yet, start from today
        LocalDate startFrom = (latestDeparture != null)
                ? latestDeparture.toLocalDate().plusDays(1)
                : today;

        if (startFrom.isAfter(target)) {
            // already have trips far enough ahead — nothing to do
            return;
        }

        generateForRange(template, startFrom, target);
    }

    private void generateForRange(ScheduleTemplate template, LocalDate from, LocalDate to) {
        Set<DayOfWeek> activeDays = parseDaysOfWeek(template.getDaysOfWeek());

        LocalDate current = from;
        int created = 0;

        while (!current.isAfter(to)) {
            if (activeDays.contains(current.getDayOfWeek())) {
                boolean exists = scheduleRepository
                        .existsByTemplateIdAndDate(template.getId(), current);

                if (!exists) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleTemplate(template);
                    schedule.setRoute(template.getRoute());
                    schedule.setBus(template.getBus());
                    schedule.setDepartureTime(
                            LocalDateTime.of(current, template.getDepartureTime()));
                    schedule.setArrivalTime(
                            LocalDateTime.of(current, template.getArrivalTime()));
                    schedule.setPrice(template.getPrice());
                    schedule.setStatus(ScheduleStatus.SCHEDULED);

                    scheduleRepository.save(schedule);
                    created++;
                }
            }
            current = current.plusDays(1);
        }

        log.info("Generated {} trips for template {}", created, template.getId());
    }

    private Set<DayOfWeek> parseDaysOfWeek(String daysOfWeek) {
        return Arrays.stream(daysOfWeek.split(","))
                .map(String::trim)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }
}