package countingsheep.alarm.ui.foreground;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import javax.inject.Inject;

import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.entities.PaymentStatus;

public class ProcessFailedPaymentsService extends Service {

    private final String TAG = getClass().getSimpleName();

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";

    public static final String CHANNEL_ID = "ForegroundServiceChannel";


    @Inject
    PaymentService paymentService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(intent != null)
//        {
//            String action = intent.getAction();
//
//            switch (action)
//            {
//                case ACTION_START_FOREGROUND_SERVICE:
//                    startForegroundService();
//                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
//                    break;
//                case ACTION_STOP_FOREGROUND_SERVICE:
//                    stopForegroundService();
//                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
//                    break;
//                case ACTION_PLAY:
//                    Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show();
//                    break;
//                case ACTION_PAUSE:
//                    Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//        return super.onStartCommand(intent, flags, startId);
        int[] alarmReactionIds = intent.getIntArrayExtra("AlarmReactionIds");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Calculating statistics.")
                //.setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        startWork(alarmReactionIds);
        
        stopForeground(true);
        stopSelf();
        return START_NOT_STICKY;
    }

    private void startWork(int[] alarmReactionIds) {
        for(int alarmReactionId : alarmReactionIds){
            paymentService.processPayment(alarmReactionId, null);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
