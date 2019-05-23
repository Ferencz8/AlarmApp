package countingsheep.alarm.ui.alarmLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.util.TimeHelper;

/**
 * RecreateAlarmsAtBootReceiver listens to the BOOT_COMPLETED event fired by the system in order to recreate all the alarms set by the user.
 */
public class RecreateAlarmsAtBootReceiver extends BroadcastReceiver {

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    AlarmService alarmService;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        Injector.getBroadcastReceiverComponent(context).inject(this);

        alarmService.getOnOrOff(new OnAsyncResponse<List<Alarm>>() {
            @Override
            public void processResponse(List<Alarm> alarmsResponse) {
                Gson gson = new Gson();
                Log.d(RecreateAlarmsAtBootReceiver.class.getSimpleName(), gson.toJson(alarmsResponse));
                if (alarmsResponse != null) {
                    for (Alarm currentAlarm : alarmsResponse) {
                        alarmLaunchHandler.registerAlarm(currentAlarm.getId(), TimeHelper.getTimeInMilliseconds(currentAlarm.getHour(), currentAlarm.getMinutes()));
                    }
                }
            }
        }, true);
    }
}


