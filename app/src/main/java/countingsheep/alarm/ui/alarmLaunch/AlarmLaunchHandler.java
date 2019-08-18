package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

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
        PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId, AlarmIntentType.Create);

        //AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAtMillis, alarmReceiverIntennt);
        //alarmManager.setAlarmClock(alarmClockInfo, alarmReceiverIntennt);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmReceiverIntennt);
    }

    /**
     * Cancel's an alarm based on the requestCode of the alarm. The requestCode is the same as the alarm id.
     * https://stackoverflow.com/questions/28922521/how-to-cancel-alarm-from-alarmmanager
     * @param alarmId
     */
    public void cancelAlarm(int alarmId){
        try{
            PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId, AlarmIntentType.Cancel);

            alarmManager.cancel(alarmReceiverIntennt);
        }
        catch(Exception ex){

        }
    }

    /**
     * Check if an alarm with the specified alarmId is already registered.
     * @param alarmId
     * @return
     */
    public boolean isAlarmUp(int alarmId){
        PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId, AlarmIntentType.CheckIfAlarmIsUp);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent("com.my.package.MY_UNIQUE_ACTION"),
                PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }

    private PendingIntent setupAlarmReceiverIntent(int requestCode, AlarmIntentType alarmIntentType){
        final Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        alarmReceiverIntent.putExtra("alarmId", requestCode);

        // Pending intent to delay intent until specific calendar time
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, requestCode, alarmReceiverIntent, getAlarmIntentType(alarmIntentType));

        return pendingIntent;
    }

    public enum AlarmIntentType{
        Create,
        CheckIfAlarmIsUp,
        Cancel
    }

    private int getAlarmIntentType(AlarmIntentType alarmIntentType){
        switch (alarmIntentType){
            case Cancel:
            case CheckIfAlarmIsUp: return PendingIntent.FLAG_NO_CREATE;
            case Create:
                default: return PendingIntent.FLAG_UPDATE_CURRENT;

        }
    }


    /**
     * Checks if the time is in the past compared to the present time of the system
     * @param millis
     * @return
     */
    private boolean isTimeInThePast(long millis){
        return Calendar.getInstance().getTimeInMillis() > millis;
    }
}
