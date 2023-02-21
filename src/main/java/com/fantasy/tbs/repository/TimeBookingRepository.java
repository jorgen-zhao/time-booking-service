package com.fantasy.tbs.repository;

import com.fantasy.tbs.domain.TimeBooking;
import feign.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data SQL repository for the TimeBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeBookingRepository extends JpaRepository<TimeBooking, Long> {

    @Query("SELECT tb.booking FROM TimeBooking tb WHERE tb.booking BETWEEN :starTime AND :endTime AND tb.personalNumber = :personalNumber")
    List<ZonedDateTime> findTbsByBookingBetween(@Param("starTime") ZonedDateTime starTime,
                                                @Param("endTime") ZonedDateTime endTime,
                                                @Param("personalNumber") String personalNumber);

    @Query("SELECT tb.personalNumber FROM TimeBooking tb WHERE tb.booking BETWEEN :starTime AND :endTime ")
    List<String> findTbsByBookingAfter(@Param("starTime") ZonedDateTime starTime,
                                            @Param("endTime") ZonedDateTime endTime);
}
