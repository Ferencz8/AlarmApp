package countingsheep.alarm.infrastructure;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;


public class NotificationHelper {

    private Activity activity;
    private NotificationManager notificationManager;


    public NotificationHelper(Activity activity) {
        this.activity = activity;
    }

    public void displayNotification(String title, String content) {

        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "RoastIncoming",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(activity, MainActivity.class);
        notificationIntent.putExtra("fragment", 2);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(activity, "RoastIncoming")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);
    }
}
