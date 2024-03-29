package countingsheep.alarm.ui.alarmLaunch;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.util.Constants;

public class AlarmRingingPlayerService extends Service {

    private Context context;
    private AlarmRingtonePlayer ringtonePlayer;
    private AlarmVibrator vibrator;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            this.context = getApplicationContext();
            ringtonePlayer = new AlarmRingtonePlayer(context);
            vibrator = new AlarmVibrator(context);

            if (intent != null) {
                String alarmDbAsJson = intent.getStringExtra("alarmDb");
                if (alarmDbAsJson != null) {
                    Alarm alarmDb = new Gson().fromJson(alarmDbAsJson, Alarm.class);
                    startAlarmRinging(alarmDb);
                }
            }

            LocalBroadcastManager.getInstance(this).registerReceiver(receiverVolumeUp,
                    new IntentFilter(Constants.Volume_Up));

            LocalBroadcastManager.getInstance(this).registerReceiver(receiverVolumeDown,
                    new IntentFilter(Constants.Volume_Down));
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        return START_STICKY;
    }

    // handler for received Intents for logout event
    private BroadcastReceiver receiverVolumeUp = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ringtonePlayer.setVolumne(true);
        }
    };

    // handler for received Intents for logout event
    private BroadcastReceiver receiverVolumeDown = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ringtonePlayer.setVolumne(false);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startAlarmRinging(Alarm alarm) {
        try {
            if (alarm.isVobrateOn()) {
                vibrator.vibrate();
            }

            Uri ringtone = Uri.parse(alarm.getRingtonePath());
            if (ringtone != null) {
                ringtonePlayer.play(ringtone, alarm.getVolume());
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    @Override
    public void onDestroy() {
        if (ringtonePlayer != null) {
            ringtonePlayer.stop();
            ringtonePlayer.cleanup();
        }
        if (vibrator != null) {
            vibrator.stop();
            vibrator.cleanup();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverVolumeUp);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverVolumeDown);
    }
}
