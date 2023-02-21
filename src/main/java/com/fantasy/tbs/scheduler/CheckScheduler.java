package com.fantasy.tbs.scheduler;


import com.fantasy.tbs.constant.TimeConstant;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.repository.TimeBookingRepository;
import com.fantasy.tbs.service.TimeBookingService;
import com.fantasy.tbs.service.impl.TimeBookingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableScheduling
@Component
public class CheckScheduler {
    private final Logger log = LoggerFactory.getLogger(TimeBookingServiceImpl.class);

    private final TimeBookingService timeBookingService;

    public CheckScheduler(TimeBookingService timeBookingService) {
        this.timeBookingService = timeBookingService;
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkMorningTime() {
        checkAndInform(TimeConstant.MORNING_CHECK_TIME);
    }


    @Scheduled(cron = "0 0 19 * * ?")
    public void checkEveningTime() {
        checkAndInform(TimeConstant.EVENING_CHECK_TIME);

    }

    /**
     * check the effective check-in user, and the rest part is missing part.
     * @param time
     */
    private void checkAndInform(ZonedDateTime time) {
        // I need all personalNumber here.
        List<String> allPersonalNumber = Arrays.asList("","");
        List<String> realCheckPersons = timeBookingService.findTbsByBookingAfter(time, ZonedDateTime.now());

        // rest part is missing time.
        allPersonalNumber.removeAll(realCheckPersons);

        // TODO using available channel to inform employee.
    }
}
