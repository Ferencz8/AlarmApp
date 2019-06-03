package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.TimeHelper;

public class AlarmLaunchActivity extends AppCompatActivity {

    private boolean isProcessing = false;
    private Activity activity;
    private ImageView snoozeImageView;
    private ImageView awakeImageView;

    private int alarmId;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmReactionService alarmReactionService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    DialogInteractor dialogInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        extractParameters(savedInstanceState);

        //CHECK API LEVEL
        //this.setShowWhenLocked(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        }

        setContentView(R.layout.activity_alarm_launch);

        bindViews();
    }

    private void extractParameters(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            alarmId = savedInstanceState.getInt("alarmId");
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                alarmId = bundle.getInt("alarmId");
            } else {
                alarmId = 0;
            }
        }
    }

    private void bindViews() {
        snoozeImageView = findViewById(R.id.snoozeImageViewId);
        snoozeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isProcessing)
                    return;

                isProcessing = true;

                //register the snooze
                alarmReactionService.add(alarmId, true, new OnAsyncResponse<Void>() {
                    @Override
                    public void processResponse(Void response) {
                        sendStopPlayerEvent();

                        isProcessing = false;
                    }
                });

                //delays the alarm
                alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
                    @Override
                    public void processResponse(Alarm alarmDb) {

                        alarmLaunchHandler.registerAlarm(alarmId, TimeHelper.getTimeInMilliseconds(alarmDb.getHour(), alarmDb.getSnoozeAmount()));
                    }
                });
            }
        });

        awakeImageView = findViewById(R.id.awakeImageViewId);
        awakeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isProcessing)
                    return;

                isProcessing = true;

                //register the awake
                alarmReactionService.add(alarmId, false, new OnAsyncResponse<Void>() {
                    @Override
                    public void processResponse(Void response) {
                        sendStopPlayerEvent();

                        isProcessing = false;
                    }
                });
            }
        });
    }

    private void sendStopPlayerEvent() {
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra("stopPlayer", true);
        sendBroadcast(intent);
    }
}
