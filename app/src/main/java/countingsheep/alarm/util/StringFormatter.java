package countingsheep.alarm.util;

/**
 * This class is used to format string and have it returned in a different manner.
 */
public class StringFormatter {

    /**
     *  Converts the int value to a string and adds in front a zero when needed
     *  E.g. 22 -> 22, 7 -> 07, 0 -> 00
     * @param value
     * @return
     * @throws Exception if the given value is not a time value. It should be between 0 and 24
     */
    public  static String getFormattedTimeDigits(int value) throws Exception {
        if(value < 0 || value >24)
            throw new Exception("The given value is not a time value. It should be between 0 and 24");

        String time = "";
        if(value < 10)
            time = "0"+value;
        else
            time = String.valueOf(value);

        return time;
    }
}
