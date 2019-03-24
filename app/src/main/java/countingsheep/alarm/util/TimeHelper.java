package countingsheep.alarm.util;

import java.util.Calendar;

public class TimeHelper {

    /**
     * Uses the hour and minute to establish a time in Milliseconds
     * @param hour
     * @param minute
     * @return
     */
    public static long getTimeInMilliseconds(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * Uses the hour and minute to establish a time in Milliseconds and also adds the dalayMinutes
     * @param hour
     * @param minute
     * @param delayMinutes
     * @return
     */
    public static long getTimeInMillisecondsAndDelayWithMinutes(int hour, int minute, int delayMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimeInMilliseconds(hour, minute));
        calendar.add(Calendar.MINUTE, delayMinutes);

        return calendar.getTimeInMillis();
    }

    /**
     * Uses the hour and minute to establish a time in Milliseconds and also adds the delayDays
     * @param hour
     * @param minute
     * @param delayDays
     * @return
     */
    public static long getTimeInMillisecondsAndDelayWithDays(int hour, int minute, int delayDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimeInMilliseconds(hour, minute));
        calendar.add(Calendar.DAY_OF_MONTH, delayDays);

        return calendar.getTimeInMillis();
    }

    /**
     * Delays the millis parameters with delayed days
     * @param millis
     * @param delayDays
     * @return
     */
    public static long delayMillisWithDays(long millis, int delayDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.add(Calendar.DAY_OF_MONTH, delayDays);

        return calendar.getTimeInMillis();
    }
}
