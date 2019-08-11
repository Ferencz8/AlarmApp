package countingsheep.alarm.ui.alarmLaunch;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.TimeService;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;
import countingsheep.alarm.util.StringFormatter;
import countingsheep.alarm.util.TimeHelper;

public class AlarmLaunchActivity extends BaseActivity {

    //https://stackoverflow.com/questions/13582395/sharing-a-variable-between-multiple-different-threads

    protected FirebaseAnalytics firebaseAnalytics;
    private volatile boolean isProcessing = false;
    private Activity activity;
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

        alarmService.getSnoozesCount(alarmId, (snoozeCount) -> {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                //Do something after 1 min
                if (isProcessing)
                    return;

                isProcessing = true;

                switch (snoozeCount) {
                    case 0: {
                        launchSnoozeProcess();
                        break;
                    }
                    case 1:
                    default: {
                        sendStopPlayerEvent();
                        activity.finish();
                        break;
                    }
                }
            }, 1000 * 60);
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
        snoozeImageView = findViewById(R.id.snoozeImageViewId);
        snoozeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                isProcessing = true;
                launchSnoozeProcess();

                if (!sharedPreferencesContainer.getShowedAskForPhoneNoPopup()) {

                    Toast.makeText(activity, "Phone No is required for Roast.", Toast.LENGTH_LONG).show();
                } else {

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
            }
        });

        awakeImageView = findViewById(R.id.awakeImageViewId);
        awakeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                Crashlytics.log(99, AlarmLaunchActivity.class.getName(), "Awake clicked");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Awake clicked");
                firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);
                isProcessing = true;

                //register the awake
                alarmReactionService.add(alarmId, false, new OnAsyncResponse<Void>() {
                    @Override
                    public void processResponse(Void response) {
                        sendStopPlayerEvent();

                        //isProcessing = false;

                        activity.finish();
                    }
                });
            }
        });

        alarmTime = findViewById(R.id.alarm_awake_time);
        alarmTitleTxtView = findViewById(R.id.alarm_name_launch);
        alarmTime.setText(TimeHelper.getCurrentShortTime());

        //TODO cu FERI
        alarmService.get(alarmId, responseAsAlarm -> {
            try {
                //alarmTime.setText(StringFormatter.getFormattedTimeDigits(responseAsAlarm.getHour()) + ":" + StringFormatter.getFormattedTimeDigits(responseAsAlarm.getMinutes()));
//                String alarmTitle = responseAsAlarm.getTitle();
//                if (!TextUtils.isEmpty(alarmTitle)) {
//                    alarmTitleTxtView.setText(alarmTitle);
//                }
                alarmTitleTxtView.setText(StringFormatter.getFormattedTimeDigits(responseAsAlarm.getHour()) + ":" + StringFormatter.getFormattedTimeDigits(responseAsAlarm.getMinutes()));
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        });
        //alarmTime.setText(String.valueOf(alarmService.get(alarmId).getHour() + ":" + alarmService.get(alarmId).getMinutes()));

        //TODO cu FERI
        //alarmTitleTxtView.setText(String.valueOf(alarmService.get(alarmId).getTitle()));
    }

    private void launchSnoozeProcess() {
        Crashlytics.log(99, AlarmLaunchActivity.class.getName(), "Snooze clicked");
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Snooze clicked");
        firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);


        //persists the snooze
        alarmReactionService.add(alarmId, true, new OnAsyncResponse<Void>() {
            @Override
            public void processResponse(Void response) {
                sendStopPlayerEvent();

                //isProcessing = false;

                activity.finish();
            }
        });

        //TODO: make this use only one db call
        //delays the alarm
        alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm alarmDb) {

                alarmService.getSnoozesCount(alarmId, new OnAsyncResponse<Integer>() {
                    @Override
                    public void processResponse(Integer response) {

                        //String debuglog = "Snooze db amount: " + alarmDb.getSnoozeAmount() + " and Snooze count: " + response;

//                        int sumOfPastAndCurrentSnoozes = alarmDb.getSnoozeAmount();
//                        if (response != 0) {
//                            sumOfPastAndCurrentSnoozes *= (response + 1);
//                        }
                        //debuglog += "Register alarm at: " + alarmDb.getHour() + " H :" + alarmDb.getMinutes() + " M with delayed min: " + sumOfPastAndCurrentSnoozes;
                        //alarmLaunchHandler.registerAlarm(alarmId, TimeHelper.getTimeInMillisecondsAndDelayWithMinutes(alarmDb.getHour(), alarmDb.getMinutes(), sumOfPastAndCurrentSnoozes));

                        long ringingTime = TimeHelper.getCurrentTimeInMilliseconds(alarmDb.getSnoozeAmount());
                        alarmLaunchHandler.registerAlarm(alarmId, ringingTime);
                        Toast.makeText(activity, TimeHelper.getTimeDifference(ringingTime), Toast.LENGTH_LONG ).show();

//                        Crashlytics.log(99, AlarmLaunchActivity.class.getName(), debuglog);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, debuglog);
//                        firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);
                    }
                });
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
