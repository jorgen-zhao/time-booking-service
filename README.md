# timeBookingService code challenge

## what I need to do ?

1. Calculate working time. ⇒ as a hr, we want to retrive how many hours an employee worked.
2. Check and inform for missing booking. ⇒ tool automatically informs when an employee forget to book his time on the same day.



## Thinking🤔

### how to calculate working time?

Here is my thinking:

**key**: We can mark two time as `start` and `end` time. Check-in time will in the middle. So when we calculate working hours, just querying time using between...and...

**steps:**

1. First in order to calculate the working time, so the employee should have two times. I just use the last one minus the first one. 

2. How to get the correct time? So I decide to create a constant like 

   ```Java
   private ZonedDateTime MORNING_CHECK_TIME = ZonedDateTime.of(LocalDate.now().atTime(8, 30), ZoneOffset.UTC);
   private ZonedDateTime EVENING_CHECK_TIME = ZonedDateTime.of(LocalDate.now().atTime(20, 30), ZoneOffset.UTC);
   ```

3. Using the `JPA` get employee's time between  `MORNING_CHECK_TIME` and `EVENING_CHECK_TIME` and make sure it must to 2 time.
4. Using this way, we can get a employee working time.(But the result contains the Lunch Break, we can do it later😄)



### Check and inform for missing booking.

Here is my thinking:

**key:** Define a effective check-in time. As if employee time at the duration. It's not missing. 

**steps:**

1. Using Java Sheduler framework to run at a definite time every day, using `cron expression` like `0 0 10,21 * * ? *`.
2. When it start, using `JPA` query every employee's time in that day, and check the time whether at the duration.
3. If a employee forget check-in in the morning, the tool will automatically informs at 10:00, evening is same too.
4. If the constant time is too late or just inform once is too little, we can change cron expression to meet our demands.

