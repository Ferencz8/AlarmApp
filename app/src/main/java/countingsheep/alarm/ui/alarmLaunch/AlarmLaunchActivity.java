package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.SMSServiceImpl;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;
import countingsheep.alarm.util.TimeHelper;

public class AlarmLaunchActivity extends BaseActivity {

    private boolean isProcessing = false;
    private Activity activity;
    private TextView snoozeImageView;
    private TextView awakeImageView;
    private TextView alarmTime;
    private TextView alarmTitle;

    private int alarmId;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmReactionService alarmReactionService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    SMSService smsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        extractParameters(savedInstanceState);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);

            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null)
                keyguardManager.requestDismissKeyguard(this, null);
        }


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }


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
                if (isProcessing)
                    return;

                isProcessing = true;

                //register the snooze
                alarmReactionService.add(alarmId, true, new OnAsyncResponse<Void>() {
                    @Override
                    public void processResponse(Void response) {
                        sendStopPlayerEvent();

                        isProcessing = false;

                        activity.finish();
                    }
                });

                //delays the alarm
                alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
                    @Override
                    public void processResponse(Alarm alarmDb) {

                        alarmLaunchHandler.registerAlarm(alarmId, TimeHelper.getTimeInMilliseconds(alarmDb.getHour(), alarmDb.getSnoozeAmount()));
                    }
                });

//                smsService.sendToSelf(new OnResult() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        Toast.makeText(activity, "Roast is on it's way!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        awakeImageView = findViewById(R.id.awakeImageViewId);
        awakeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                isProcessing = true;

                //register the awake
                alarmReactionService.add(alarmId, false, new OnAsyncResponse<Void>() {
                    @Override
                    public void processResponse(Void response) {
                        sendStopPlayerEvent();

                        isProcessing = false;

                        activity.finish();
                    }
                });
            }
        });

        alarmTime = findViewById(R.id.alarm_awake_time);
        //TODO cu FERI
        //alarmTime.setText(String.valueOf(alarmService.get(alarmId).getHour() + ":" + alarmService.get(alarmId).getMinutes()));

        alarmTitle = findViewById(R.id.alarm_name_launch);
        //TODO cu FERI
        //alarmTitle.setText(String.valueOf(alarmService.get(alarmId).getTitle()));
    }

    private void sendStopPlayerEvent() {
        this.activity.stopService(new Intent(this.activity, AlarmRingingPlayerService.class));
    }

    //TODO
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Intent intent = new Intent(Constants.Volume_Down);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Intent intent = new Intent(Constants.Volume_Up);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        super.onKeyLongPress(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //TODO
            return true;
        }
        return false;
    }
}
