package countingsheep.alarm.ui.alarmLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.util.TimeHelper;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private AlarmRingtonePlayer ringtonePlayer;
    private AlarmVibrator vibrator;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.e("In receiver", "Yay!");

        Injector.getBroadcastReceiverComponent(context).inject(this);

        int alarmId = intent.getExtras().getInt("alarmId");
        alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm alarmDb) {
                if(alarmDb == null)
                    return;

                if(shouldAlarmBeStarted(alarmDb)){
                    Intent intent = new Intent(context, AlarmRingingPlayerService.class);
                    intent.putExtra("alarmDb", new Gson().toJson(alarmDb));
                    context.startService(intent);
                    startAlarmUserExperience(alarmDb.getId());
                }
                else{
                    //this needs to send current day,hour,minute, without snooze
                    alarmLaunchHandler.registerAlarm(alarmDb.getId(), TimeHelper.getTimeInMilliseconds(alarmDb.getHour(), alarmDb.getMinutes()));
                }
            }
        });
    }

    private boolean shouldAlarmBeStarted(Alarm alarmDb) {

        boolean shouldAlarmBeStarted = false;

        String repeatDays = alarmDb.getRepeatDays();

        if(TextUtils.isEmpty(repeatDays))
            return true;

        Calendar calendar = Calendar.getInstance();

        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today) {
            case Calendar.MONDAY:
                if(repeatDays.contains("Mo")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
            case Calendar.TUESDAY:
                if(repeatDays.contains("Tu")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
            case Calendar.WEDNESDAY:
                if(repeatDays.contains("We")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
            case Calendar.THURSDAY:
                if(repeatDays.contains("Th")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;

            case Calendar.FRIDAY:
                if(repeatDays.contains("Fr")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
            case Calendar.SATURDAY:
                if(repeatDays.contains("Sa")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
            case Calendar.SUNDAY:
                if(repeatDays.contains("Su")){
                    shouldAlarmBeStarted = true;
                }
                else{
                    shouldAlarmBeStarted = false;
                }
                break;
        }

        return shouldAlarmBeStarted;
    }

    private void startAlarmUserExperience(int alarmId){
        // Create intent
        Intent alarmintent = new Intent(context, AlarmLaunchActivity.class);
        alarmintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Pass alarmId
        alarmintent.putExtra("alarmId", alarmId);

        // Start ringtone service
        context.startActivity(alarmintent);
    }
}
