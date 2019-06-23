package countingsheep.alarm.core.services;


import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeService {

    private static final String GMT = "gmt";

    @Inject
    public TimeService() { }

    public Date getUTCDateNow(){
        Date date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant instant = Instant.now();
            date = Date.from(instant);
        }
        else{
            DateTime dateTime = new DateTime( DateTimeZone.UTC);
            date = dateTime.toDate();
        }
        return date;
    }


    public Date getDateFromString(String dateAsString){
        //String dtStart = "2010-10-15T09:27:37Z";
        //"yyyy-MM-dd'T'HH:mm:ss'Z'"
        if (TextUtils.isEmpty(dateAsString)) {
            return null; // or break, continue, throw
        }

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = format.parse(dateAsString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            return null;
        }
    }
}
