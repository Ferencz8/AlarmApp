//package countingsheep.alarm.ui.alarmLaunch;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import countingsheep.alarm.Injector;
//import countingsheep.alarm.R;
//import countingsheep.alarm.core.services.interfaces.AlarmService;
//import countingsheep.alarm.db.entities.Alarm;
//import countingsheep.alarm.util.TimeHelper;
//
//public class AlarmBootService extends Service {
//
//    @Inject
//    AlarmLaunchHandler alarmLaunchHandler;
//
//    @Inject
//    AlarmService alarmService;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//
//        Log.d(AlarmBootService.class.getSimpleName(), "Started Service");
//        super.onCreate();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            Notification.Builder builder = new Notification.Builder(this, AlarmBootService.class.getCanonicalName())
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText("Resetting Alarms")
//                    .setSmallIcon(R.drawable.ic_icon_clock);
//
//            Notification notification = builder.build();
//            startForeground(1, notification);
//
//        } else {
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText("Resetting Alarms")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true);
//
//            Notification notification = builder.build();
//            startForeground(1, notification);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//        Log.d(AlarmBootService.class.getSimpleName(), "Started Service - From Command");
//
//        Injector.getServiceComponent(this.getBaseContext()).inject(this);
//
//        List<Alarm> onAlarms = alarmService.getOnOrOff(true);
//        Gson gson = new Gson();
//        Log.d(RecreateAlarmsAtBootReceiver.class.getSimpleName(), gson.toJson(onAlarms));
//
//        for (Alarm currentAlarm : onAlarms) {
//            alarmLaunchHandler.registerAlarm(currentAlarm.getId(), TimeHelper.getTimeInMilliseconds(currentAlarm.getHour(), currentAlarm.getMinutes()));
//        }
//
//        stopSelf();
//        return START_NOT_STICKY;
//    }
//}