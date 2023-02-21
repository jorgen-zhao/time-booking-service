package com.fantasy.tbs.constant;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Define a constant to refer later in the coding, avoid confusing time definition.
 */
public interface TimeConstant {

    /**
     * early check-in time, make sure ahead of the real check-in time.
     * So can using between...and... to calculate working time
     */
    ZonedDateTime MORNING_CHECK_TIME = ZonedDateTime.of(LocalDate.now().atTime(8, 30), ZoneOffset.UTC);
    ZonedDateTime EVENING_CHECK_TIME = ZonedDateTime.of(LocalDate.now().atTime(20, 30), ZoneOffset.UTC);


    /**
     * Define a lunch break time, real working time not contain it.
     */
    Long LUNCH_BREAK_TIME = 1L;

    /**
     * standard check time every day.
     */
    int STANDARD_CHECK_TIMES = 2;
}
