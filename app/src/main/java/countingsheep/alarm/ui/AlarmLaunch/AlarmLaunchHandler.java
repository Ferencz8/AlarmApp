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

    public void registerAlarm(long triggerAtMilis){

        final Intent alarmReceiverIntent = new Intent(activity, AlarmReceiver.class);

        // Pending intent to delay intent until specific calendar time
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (activity, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set alarm manager
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMilis, pendingIntent);
    }
}
