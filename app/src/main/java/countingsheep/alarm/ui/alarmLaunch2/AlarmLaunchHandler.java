package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.ALARM_SERVICE;


@Singleton
public class AlarmLaunchHandler {

    private Activity activity;
    private AlarmManager alarmManager;

    @Inject
    public AlarmLaunchHandler(Activity activity) {
        this.activity = activity;
        this.alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
    }

    public void registerAlarm(int alarmId, long triggerAtMilis){

        PendingIntent alarmReceiverIntennt = setupAlarmReceiverIntent(alarmId);

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMilis, alarmReceiverIntennt);
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
        final Intent alarmReceiverIntent = new Intent(activity, AlarmReceiver.class);

        // Pending intent to delay intent until specific calendar time
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (activity, requestCode, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
