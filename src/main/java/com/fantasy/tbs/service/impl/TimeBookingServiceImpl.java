package com.fantasy.tbs.service.impl;

import com.fantasy.tbs.constant.TimeConstant;
import com.fantasy.tbs.domain.TimeBookDTO;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.repository.TimeBookingRepository;
import com.fantasy.tbs.service.TimeBookingService;
import com.fantasy.tbs.service.mapper.TimeBookMapper;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TimeBooking}.
 */
@Service
@Transactional
public class TimeBookingServiceImpl implements TimeBookingService {

    private final Logger log = LoggerFactory.getLogger(TimeBookingServiceImpl.class);

    private final TimeBookingRepository timeBookingRepository;
    private final TimeBookMapper timeBookMapper;

    public TimeBookingServiceImpl(TimeBookingRepository timeBookingRepository, TimeBookMapper timeBookMapper) {
        this.timeBookingRepository = timeBookingRepository;
        this.timeBookMapper = timeBookMapper;
    }

    @Override
    public TimeBooking save(TimeBooking timeBooking) {
        log.debug("Request to save TimeBooking : {}", timeBooking);
        return timeBookingRepository.save(timeBooking);
    }

    @Override
    public Optional<TimeBooking> partialUpdate(TimeBooking timeBooking) {
        log.debug("Request to partially update TimeBooking : {}", timeBooking);

        return timeBookingRepository
            .findById(timeBooking.getId())
            .map(
                existingTimeBooking -> {
                    if (timeBooking.getBooking() != null) {
                        existingTimeBooking.setBooking(timeBooking.getBooking());
                    }
                    if (timeBooking.getPersonalNumber() != null) {
                        existingTimeBooking.setPersonalNumber(timeBooking.getPersonalNumber());
                    }

                    return existingTimeBooking;
                }
            )
            .map(timeBookingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeBooking> findAll() {
        log.debug("Request to get all TimeBookings");
        return timeBookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeBooking> findOne(Long id) {
        log.debug("Request to get TimeBooking : {}", id);
        return timeBookingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeBooking : {}", id);
        timeBookingRepository.deleteById(id);
    }

    @Override
    public void bookTime(TimeBookDTO timeBookDTO) {
        timeBookingRepository.save(timeBookMapper.toTimeBooking(timeBookDTO));
    }

    @Override
    public Long calculateWorkingTime(String personalNumber) {
        List<ZonedDateTime> timeBookingList = timeBookingRepository.findTbsByBookingBetween(TimeConstant.MORNING_CHECK_TIME,
            TimeConstant.EVENING_CHECK_TIME, personalNumber);
        // make sure there is two.
        if (Collections.isEmpty(timeBookingList)) {
            log.error("{} dont have check data ", personalNumber);
            return null;
        }
        if (timeBookingList.size() > TimeConstant.STANDARD_CHECK_TIMES) {
            log.error("{} missing one check time", personalNumber);
            return null;
        }
        // now we can make sure employee has 2 time.
        ZonedDateTime earlyRealCheckTime = timeBookingList.get(0);
        ZonedDateTime laterRealCheckTime = timeBookingList.get(1);
        Duration duration = Duration.between(earlyRealCheckTime, laterRealCheckTime);

        // we can minus the LUNCH_BREAK_TIME;
        return duration.toHours();
    }

    @Override
    public List<String> findTbsByBookingAfter(ZonedDateTime earlyCheckTime, ZonedDateTime defineTime) {
        return timeBookingRepository.findTbsByBookingAfter(earlyCheckTime, defineTime);
    }
}
