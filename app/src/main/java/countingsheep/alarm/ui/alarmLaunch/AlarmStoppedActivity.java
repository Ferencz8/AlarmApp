package countingsheep.alarm.ui.alarmLaunch;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import countingsheep.alarm.R;
import countingsheep.alarm.ui.BaseActivity;

public class AlarmStoppedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_stopped);

        ((TextView) findViewById(R.id.dialog)).setTypeface(
                Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf")
        );
    }
}