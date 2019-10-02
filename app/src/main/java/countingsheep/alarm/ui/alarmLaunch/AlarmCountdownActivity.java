package countingsheep.alarm.ui.alarmLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.NotificationHelper;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.util.StringFormatter;
import countingsheep.alarm.util.TimeHelper;
import io.fabric.sdk.android.services.common.Crash;

public class AlarmCountdownActivity extends BaseActivity {


    private TextView countdownTimerView;
    private TextView refundBtn;
    private ProgressBar timerCircle;

    private int timeCountdownInSeconds = 3;
    private long timeCountInMilliSeconds;
    private CountDownTimer countDownTimer;

    private int alarmId;
    private volatile boolean isProcessing = false;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmReactionService alarmReactionService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);

        extractParameters(savedInstanceState);

        setContentView(R.layout.activity_alarm_launch_refund);

        bindViews();

        startCountDownTimer();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    int secondsLeft = (int) millisUntilFinished / 1000;
                    countdownTimerView.setText("00:" + StringFormatter.getFormattedTimeDigits(secondsLeft));

                    timerCircle.setProgress((int) millisUntilFinished / 1000);
                } catch (Exception ex) {
                    Crashlytics.logException(ex);
                }
            }

            @Override
            public void onFinish() {
                countdownTimerView.setText("00:00");
                timerCircle.setProgress(0);
                launchSnoozeProcess();
            }
        };
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    private void bindViews() {
        countdownTimerView = findViewById(R.id.timerTextViewId);
        countdownTimerView.setText("00:03");
        timeCountInMilliSeconds = timeCountdownInSeconds * 1000;
        timerCircle = findViewById(R.id.timerCircleId);
        timerCircle.setMax((int) timeCountInMilliSeconds / 1000);
        timerCircle.setProgress((int) timeCountInMilliSeconds / 1000);

        refundBtn = findViewById(R.id.nevermindSnoozeId);
        refundBtn.setText("Nevermind, I'm awake.");
        refundBtn.setOnClickListener(getRefundListener());
    }

    private View.OnClickListener getRefundListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessing)
                    return;

                isProcessing = true;
                stopCountDownTimer();

                Crashlytics.log(99, AlarmLaunchActivity.class.getName(), "Awake clicked");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Awake clicked");
                firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);

                //TODO:: duplicate code with AlarmLaunchActivity - future refactor
                //register again if repeatable
                alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
                    @Override
                    public void processResponse(Alarm alarmDb) {
                        if (!alarmDb.getRepeatDays().isEmpty()) {
                            alarmLaunchHandler.registerAlarm(alarmId, TimeHelper.getTimeInMillisecondsAndDelayWithDays(alarmDb.getHour(), alarmDb.getMinutes(), 1));
                        }
                        else{
                            alarmService.switchOnOf(alarmId, false);
                        }
                        //register the awake
                        alarmReactionService.add(alarmId, false, new OnAsyncResponse<Void>() {
                            @Override
                            public void processResponse(Void response) {

                                redirectToMainScreen();
                                //redirectToAwakeScreen();
                            }
                        });
                    }
                });
            }
        };
    }

    private void launchSnoozeProcess() {
        Crashlytics.log(99, AlarmLaunchActivity.class.getName(), "Snooze clicked");
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Snooze clicked");
        firebaseAnalytics.logEvent("AlarmLaunchActivity", bundle);

        //delays the alarm
        alarmService.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm alarmDb) {

                long ringingTime = TimeHelper.getCurrentTimeInMilliseconds(alarmDb.getSnoozeAmount());
                alarmLaunchHandler.registerAlarm(alarmId, ringingTime);
                Toast.makeText(activity, TimeHelper.getTimeDifference(ringingTime), Toast.LENGTH_LONG).show();

                //register the snooze
                alarmReactionService.add(alarmId, true, null);

                messageService.getRoastMessage(new OnResult<Message>() {
                    @Override
                    public void onSuccess(Message result) {
                        result.setSeen(true);

                        messageService.add(result, new OnAsyncResponse<Long>() {
                            @Override
                            public void processResponse(Long response) {
//                                NotificationHelper notificationHelper = new NotificationHelper(activity);
//                                notificationHelper.displayNotification("Your roast is here!!", "");
//
                                //redirectToMainScreen();

                                redirectToRoastScreen(result.getId(), result.getContent());
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {

                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        redirectToMainScreen();
                    }
                });
            }
        });
    }

    private void redirectToMainScreen(){

        finish();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.popBackStack();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void redirectToAwakeScreen() {
        finish();
        startActivity(new Intent(this, AlarmStoppedActivity.class));
    }

    private void redirectToRoastScreen(int id, String roast) {
        finish();
        Intent intent = new Intent(this, AlarmRoastActivity.class);
        intent.putExtra(AlarmRoastActivity.ARG_EXTRA_MSG_ID, id);
        intent.putExtra(AlarmRoastActivity.ARG_EXTRA_ROAST, roast);
        startActivity(intent);
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
}
