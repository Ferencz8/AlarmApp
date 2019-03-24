package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.util.TimeHelper;

import static android.content.Context.ALARM_SERVICE;


@Singleton
public class AlarmLaunchHandler {

    private Context context;
    private AlarmManager alarmManager;

    @Inject
    public AlarmLaunchHandler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    public void registerAlarm(int alarmId, long triggerAtMillis){

        if(isTimeInThePast(triggerAtMillis)){
            triggerAtMillis = TimeHelper.delayMillisWithDays(triggerAtMillis, 1);
        }

        PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmReceiverIntennt);
    }

    /**
     * Cancel's an alarm based on the requestCode of the alarm. The requestCode is the same as the alarm id.
     * https://stackoverflow.com/questions/28922521/how-to-cancel-alarm-from-alarmmanager
     * @param alarmId
     */
    public void cancelAlarm(int alarmId){
        PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId);

        alarmManager.cancel(alarmReceiverIntennt);
    }

    private PendingIntent setupAlarmReceiverIntent(int requestCode){
        final Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        alarmReceiverIntent.putExtra("alarmId", requestCode);

        // Pending intent to delay intent until specific calendar time
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, requestCode, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Checks if the time is in the past compared to the present time of the system
     * @param millis
     * @return
     */
    private boolean isTimeInThePast(long millis){
        return System.currentTimeMillis() > millis;
    }
}
