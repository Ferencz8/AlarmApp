package countingsheep.alarm.ui.alarmLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

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
        Log.e("In receiver", "Yay!");

        initalize(context, intent);

        int alarmId = intent.getExtras().getInt("alarmId");
        alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm alarmDb) {
                if(alarmDb == null)
                    return;

                if(shouldAlarmBeStarted(alarmDb)){
                    startAlarmRinging(alarmDb);
                    startAlarmUserExperience(alarmDb.getId());
                }
                else{

                    alarmLaunchHandler.registerAlarm(alarmDb.getId(), TimeHelper.getTimeInMillisecondsAndDelayWithMinutes(alarmDb.getHour(), alarmDb.getMinutes(), alarmDb.getSnoozeAmount()));
                }
            }
        });
    }

    private void startAlarmRinging(Alarm alarm){
        if(alarm.isVobrateOn()){
            vibrator.vibrate();
        }

        Uri ringtone = Uri.parse(alarm.getRingtonePath());
        if(ringtone != null){
            ringtonePlayer.play(ringtone);
        }
    }

    private void initalize(Context context, Intent intent){


        if(!shouldAlarmBeStopped(intent)) {//the alarm has not yet been started
            this.context = context;
            ringtonePlayer = new AlarmRingtonePlayer(context);
            vibrator = new AlarmVibrator(context);

            Injector.getBroadcastReceiverComponent(context).inject(this);
        }
        else{//the alarm is already running
            ringtonePlayer.stop();
            vibrator.stop();
            ringtonePlayer.cleanup();
            vibrator.cleanup();
        }
    }

    private boolean shouldAlarmBeStopped(Intent intent){
        try {
            boolean stopAlarm = intent.getExtras().getBoolean("stopPlayer");
            return stopAlarm;
        }
        catch(Exception e){
            return false;
        }
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
