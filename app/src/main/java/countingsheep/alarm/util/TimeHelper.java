package countingsheep.alarm.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeHelper {

    /**
     * Uses the hour and minute to establish a time in Milliseconds
     *
     * @param hour
     * @param minute
     * @return
     */
    public static long getTimeInMilliseconds(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static boolean isTimeInThePast(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Uses the hour and minute to establish a time in Milliseconds and also adds the dalayMinutes
     *
     * @param hour
     * @param minute
     * @param delayMinutes
     * @return
     */
    public static long getTimeInMillisecondsAndDelayWithMinutes(int hour, int minute, int delayMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimeInMilliseconds(hour, minute));
        calendar.add(Calendar.MINUTE, delayMinutes);

        return calendar.getTimeInMillis();
    }

    /**
     * Uses the hour and minute to establish a time in Milliseconds and also adds the delayDays
     *
     * @param hour
     * @param minute
     * @param delayDays
     * @return
     */
    public static long getTimeInMillisecondsAndDelayWithDays(int hour, int minute, int delayDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimeInMilliseconds(hour, minute));
        calendar.add(Calendar.HOUR_OF_DAY, 24* delayDays);

        return calendar.getTimeInMillis();
    }

    /**
     * Delays the millis parameters with delayed days
     *
     * @param millis
     * @param delayDays
     * @return
     */
    public static long delayMillisWithDays(long millis, int delayDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.add(Calendar.HOUR_OF_DAY, 24* delayDays);

        return calendar.getTimeInMillis();
    }

    /**
     * Delays the current time with the specified minutes and returns the current time in milliseconds
     *
     * @param minutes
     * @return
     */
    public static long getCurrentTimeInMilliseconds(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTimeInMillis();
    }

    /**
     * Returns as a formatted string the difference between the future time and the current time
     *
     * @param futureTimeInMillis
     * @return
     */
    public static String getTimeDifference(long futureTimeInMillis) {

        String diffFormated = "";
        try {
            Calendar calendar = Calendar.getInstance();
            long currentTimeInMillis = calendar.getTimeInMillis();
            long diff = Math.abs(futureTimeInMillis - currentTimeInMillis);
            int Hours = (int) (diff / (1000 * 60 * 60));
            int Mins =  Math.round ((diff / (1000 * 60)) % 60);
            int days = Hours / 24;
            if (days > 0) {
                Hours = Hours - days * 24;

                diffFormated = "Alarm will ring in " + days + " days " + StringFormatter.getFormattedTimeDigits(Hours) + " h :" + StringFormatter.getFormattedTimeDigits(Mins) + " m";

            } else {
                if (Hours <= 0 & Mins <= 0) {
                    diffFormated = "Alarm will ring in less than 1 min";
                } else {
                    diffFormated = "Alarm will ring in " + StringFormatter.getFormattedTimeDigits(Hours) + " h :" + StringFormatter.getFormattedTimeDigits(Mins) + " m";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffFormated;
    }


    private static class RepeatDayOfWeek {
        public boolean IsRepeatable;
        public int DayOfWeek;

        public RepeatDayOfWeek(boolean isRepeatable, int dayOfWeek) {
            IsRepeatable = isRepeatable;
            DayOfWeek = dayOfWeek;
        }
    }

    public static int getDaysUntilRepeatDay(String repeatDays) {
        if (TextUtils.isEmpty(repeatDays))
            return 0;

        int daysCount = 0;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        List<RepeatDayOfWeek> repeasDaysList = new ArrayList<>();

        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Su"), Calendar.SUNDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Mo"), Calendar.MONDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Tu"), Calendar.TUESDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("We"), Calendar.WEDNESDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Th"), Calendar.THURSDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Fr"), Calendar.FRIDAY));
        repeasDaysList.add(new RepeatDayOfWeek(repeatDays.contains("Sa"), Calendar.SATURDAY));

        for (int index = today - 1; index < repeasDaysList.size(); index++) {
            RepeatDayOfWeek repeatDayOfWeek = repeasDaysList.get(index);
            if (!repeatDayOfWeek.IsRepeatable)
                daysCount++;
            else
                return daysCount;
        }
        if (today != 1) {
            for (int index = 0; index < today - 1; index++) {
                RepeatDayOfWeek repeatDayOfWeek = repeasDaysList.get(index);
                if (!repeatDayOfWeek.IsRepeatable)
                    daysCount++;
                else
                    return daysCount;
            }
        }
        return daysCount;
    }

    public static String getCurrentShortTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat shortdf = new SimpleDateFormat("HH:mm");

        return shortdf.format(calendar.getTime());
    }

    public static String getDateStringFromDateObject(Date date) {
        return date == null
                ? null
                :   new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);
    }

    public static String getHourStringFromDateObject(Date date) {
        return date == null
                ? null
                : new SimpleDateFormat("HH:mm", Locale.US).format(date);
    }
}
