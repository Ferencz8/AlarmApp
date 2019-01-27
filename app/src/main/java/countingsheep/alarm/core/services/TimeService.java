package countingsheep.alarm.core.services;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

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
}
