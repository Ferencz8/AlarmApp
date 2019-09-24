package countingsheep.alarm.ui.alarmLaunch;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;
import countingsheep.alarm.util.TimeHelper;

public class AlarmLaunchActivity extends BaseActivity {

    //https://stackoverflow.com/questions/13582395/sharing-a-variable-between-multiple-different-threads

    protected FirebaseAnalytics firebaseAnalytics;
    private volatile boolean isProcessing = false;
    private TextView snoozeImageView;
    private TextView awakeImageView;
    private TextView alarmTime;
    private TextView alarmTitleTxtView;
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

    @Inject
    MessageService messageService;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);


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

        initAutomaticSnooze();
    }

    private void initAutomaticSnooze() {

//        alarmService.getSnoozesCount(alarmId, (snoozeCount) -> {
//
//            final Handler handler = new Handler();
//            handler.postDelayed(() -> {
//                if (isProcessing)
//                    return;
//
//                isProcessing = true;
//
//                switch (snoozeCount) {
//                    case 0: {
//                        delayTheAlarmForFree();
//                        break;
//                    }
//                    case 1:
//                    default: {
//                        sendStopPlayerEvent();
//                        activity.finish();
//                        break;
//                    }
//                }
//            }, 1000 * 60 * 3);//3 min
//        });

        boolean canDelay = sharedPreferencesContainer.getDelayTheAlarmForFree();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                if (isProcessing)
                    return;

                isProcessing = true;

                sendStopPlayerEvent();

                //delays the alarm for free only once
                if (canDelay) {
                    sharedPreferencesContainer.setDelayTheAlarmForFree(false);
                    delayTheAlarmForFree();
                } else {
                    sharedPreferencesContainer.setDelayTheAlarmForFree(true);
                }
                activity.finish();
            }
            catch (Exception ex){
                Crashlytics.logException(ex);
            }
        }, 1000 * 60 * 3);//3 min
    }

    private void delayTheAlarmForFree() {

        //delays the alarm
        alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm alarmDb) {

                long ringingTime = TimeHelper.getCurrentTimeInMilliseconds(alarmDb.getSnoozeAmount());
                alarmLaunchHandler.registerAlarm(alarmId, ringingTime);
                Toast.makeText(activity, TimeHelper.getTimeDifference(ringingTime), Toast.LENGTH_LONG).show();
            }
        });
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

        displayAlarmInfo();

        snoozeImageView = findViewById(R.id.snoozeImageViewId);
        snoozeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                isProcessing = true;

                sendStopPlayerEvent();

                sharedPreferencesContainer.setDelayTheAlarmForFree(true);

                activity.finish();
                Intent intent = new Intent(AlarmLaunchActivity.this, AlarmCountdownActivity.class);
                intent.putExtra("alarmId", alarmId);
                startActivity(intent);
            }
        });

        awakeImageView = findViewById(R.id.awakeImageViewId);
        awakeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                isProcessing = true;
                Crashlytics.log(99, AlarmLaunchActivity.class.getName(), "Awake clicked");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Awake clicked");
                firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);

                sendStopPlayerEvent();

                sharedPreferencesContainer.setDelayTheAlarmForFree(true);

                alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
                    @Override
                    public void processResponse(Alarm alarmDb) {

                        if(!alarmDb.getRepeatDays().isEmpty()){
                            alarmLaunchHandler.registerAlarm(alarmId,  TimeHelper.getTimeInMillisecondsAndDelayWithDays(alarmDb.getHour(), alarmDb.getMinutes(), 1));
                        }

                        //register the awake
                        alarmReactionService.add(alarmId, false, new OnAsyncResponse<Void>() {
                            @Override
                            public void processResponse(Void response) {

                                activity.finish();
                                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        });
                    }
                });
            }
        });
    }

    private void displayAlarmInfo() {
        alarmTime = findViewById(R.id.alarm_awake_time);
        alarmTitleTxtView = findViewById(R.id.alarm_name_launch);
        alarmTime.setText(TimeHelper.getCurrentShortTime());

        alarmService.get(alarmId, responseAsAlarm -> {
            try {
                String alarmTitle = responseAsAlarm.getTitle();
                if (!TextUtils.isEmpty(alarmTitle)) {
                    alarmTitleTxtView.setText(alarmTitle);
                }

            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        });
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

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
