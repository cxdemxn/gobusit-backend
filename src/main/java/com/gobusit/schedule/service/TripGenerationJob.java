package com.gobusit.schedule.service;

import com.gobusit.schedule.repository.ScheduleTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TripGenerationJob {

    private final ScheduleTemplateRepository templateRepository;
    private final TripGenerationService tripGenerationService;

    // Runs every night at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void generateUpcomingTrips() {
        log.info("Nightly trip generation job starting...");

        templateRepository.findByActiveTrue()
                .forEach(tripGenerationService::extendTripsForTemplate);

        log.info("Nightly trip generation job complete.");
    }
}