package countingsheep.alarm.ui.AlarmLaunch;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import countingsheep.alarm.R;

public class AlarmLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //CHECK API LEVEL
        //this.setShowWhenLocked(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }

        setContentView(R.layout.activity_alarm_launch);

        Toast.makeText(this,  "Alarm has been launched", Toast.LENGTH_LONG);
    }
}
