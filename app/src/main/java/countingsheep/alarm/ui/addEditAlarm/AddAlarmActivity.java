package countingsheep.alarm.ui.addEditAlarm;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.kevalpatel.ringtonepicker.RingtoneUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.foreground.ProcessFailedPaymentsService;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.StringFormatter;
import countingsheep.alarm.util.TimeHelper;

public class AddAlarmActivity extends BaseActivity implements View.OnClickListener {
    protected FirebaseAnalytics firebaseAnalytics;
    private Alarm alarm;
    private Activity activity;

    AlarmDayRecyclerViewDataAdapter adapter;
    private List<AlarmDayRecyclerViewItem> daysList = null;
    private List<String> weekDays = Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su");
    private Switch vibrateSwitch;
    private SeekBar volumeSeekBar;
    private TextView saveImageView;
    private TextView selectedRingtoneTextView;
    private TextView timeView;
    private TextView snoozeTv;
    private TextView selectedSnooze;
    private ArrayList<Integer> durations = new ArrayList<Integer>();
    private EditText titleView;
    private Dialog snoozeDialog;
    RecyclerView snoozeRv;
    SnoozeAdapter snoozeAdapter;

    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    private boolean isEdit = false;
    private int seebBarProgress;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    PaymentDetailsRepository paymentDetailsRepository;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        activity = this;

        Injector.getActivityComponent(this).inject(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setupAlarm();

        bindViews();
        durations.add(1);
        durations.add(5);
        durations.add(10);
        durations.add(15);
        durations.add(30);
    }

    private void setupAlarm() {
        alarm = getAlarmFromIntent();
        if (alarm != null) {

            isEdit = true;
        } else {
            alarm = new Alarm();
        }
    }


    private Alarm getAlarmFromIntent() {
        if (getIntent().getExtras() == null)
            return null;
        Serializable serializedAlarm = getIntent().getExtras().getSerializable("alarm");

        if (serializedAlarm == null)
            return null;

        return (Alarm) serializedAlarm;
    }


    private void bindViews() {


        RecyclerView recyclerView = findViewById(R.id.daysRecyclerView);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);

        // Set layout manager.
        recyclerView.setLayoutManager(layoutManager);

        // Create car recycler view data adapter with car item list.
        adapter = new AlarmDayRecyclerViewDataAdapter(this, daysList);
        // Set data adapter.
        recyclerView.setAdapter(adapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerView.scrollToPosition(1);

        timeView = findViewById(R.id.timeTextID);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    dialogInteractor.displayTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            alarm.setHour(hourOfDay);
                            alarm.setMinutes(minute);
                            String time = getFormattedTime(hourOfDay, minute);
                            timeView.setText(time);
                        }
                    }, alarm.getHour(), alarm.getMinutes());
                } else {
                    dialogInteractor.displayTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            alarm.setHour(hourOfDay);
                            alarm.setMinutes(minute);
                            String time = getFormattedTime(hourOfDay, minute);
                            timeView.setText(time);
                        }
                    });
                }
            }
        });
        titleView = findViewById(R.id.titleEditTextId);
        vibrateSwitch = findViewById(R.id.vibrateSwitchId);
        volumeSeekBar = findViewById(R.id.volumeSeekBarId);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seebBarProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        snoozeTv = (TextView) findViewById(R.id.snoozeTextView);
        selectedSnooze = (TextView) findViewById(R.id.selectedSnooze);
        createSnoozeDialog();
        setDefaultSnoozeValue();

        snoozeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snoozeDialog.show();
            }
        });
        //bind week days
        if (daysList == null) {
            daysList = new ArrayList<>();
            for (String day : weekDays) {
                daysList.add(getAdarmDayViewItem(day));
            }
            adapter.set(daysList);
        }

        selectedRingtoneTextView = findViewById(R.id.selectedRingtoneTextView);
        selectedRingtoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEdit) {
                    tryShowRingtonePicker(Uri.parse(alarm.getRingtonePath()));
                } else {
                    tryShowRingtonePicker(null);
                }
            }
        });
        setDefaultRingtoneValues();

        saveImageView = findViewById(R.id.add_alarm_save_button);
        saveImageView.setOnClickListener(getSaveAlarmClickListener());

        String time;
        if (isEdit) {
            time = this.getFormattedTime(alarm.getHour(), alarm.getMinutes());

            selectedRingtoneTextView.setText(alarm.getRingtoneName());

            adapter.markDaysAsSelected(Arrays.asList(alarm.getRepeatDays().split(",")));

            vibrateSwitch.setChecked(alarm.isVobrateOn());
        } else {
            time = this.getFormattedTime(7, 5);
        }
        timeView.setText(time);
        titleView.setText(alarm.getTitle());
        vibrateSwitch.setChecked(alarm.isVobrateOn());
        volumeSeekBar.setProgress(alarm.getVolume());

        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        backBtn.setOnClickListener(this);
        titleTv.setText("Set alarm");
    }

    private void setDefaultRingtoneValues() {
        try {
            Uri systemRingtoneUri = RingtoneUtils.getSystemRingtoneTone();
            String systemRingtoneName = RingtoneUtils.getRingtoneName(this, systemRingtoneUri);

            alarm.setRingtonePath(systemRingtoneUri.toString());
            alarm.setRingtoneName(systemRingtoneName);
            selectedRingtoneTextView.setText(systemRingtoneName);
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }

    private void setDefaultSnoozeValue() {
        snoozeAdapter.selectedItem = 0; //1 minute
        selectedSnooze.setText(getString(R.string.DefaultSnoozeDisplayedValue));
    }

    private AlarmDayRecyclerViewItem getAdarmDayViewItem(String day) {
        AlarmDayRecyclerViewItem item = new AlarmDayRecyclerViewItem();
        item.setText(day);
        return item;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.showRingtonePicker(Uri.parse(alarm.getRingtonePath()));
                } else {
                    this.dialogInteractor.displayReactiveDialog("Permission Impact", "The permission needs to be granted in order for the app to be allowed to show ringtones", null);
                }
                return;
            }
        }
    }

    /**
     * https://github.com/kevalpatel2106/android-ringtone-picker
     *
     * @param currentRingtone
     */
    private void tryShowRingtonePicker(Uri currentRingtone) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // Permission has already been granted
            this.showRingtonePicker(currentRingtone);
        }
    }

    private void showRingtonePicker(Uri currentRingtone) {
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                .Builder(this, getSupportFragmentManager())

                //Set title of the dialog.
                //If set null, no title will be displayed.
                .setTitle("Select ringtone")

                //set the currently selected uri, to mark that ringtone as checked by default.
                //If no ringtone is currently selected, pass null.
                .setCurrentRingtoneUri(currentRingtone)

                //Set true to allow allow user to select default ringtone set in phone settings.
                //.displayDefaultRingtone(true)

                //Set true to allow user to select silent (i.e. No ringtone.).
                //.displaySilentRingtone(true)

                //set the text to display of the positive (ok) button.
                //If not set OK will be the default text.
                .setPositiveButtonText("SET RINGTONE")

                //set text to display as negative button.
                //If set null, negative button will not be displayed.
                .setCancelButtonText("CANCEL")

                //Set flag true if you want to play the sample of the clicked tone.
                .setPlaySampleWhileSelection(true)

                //Set the callback listener.
                .setListener(new RingtonePickerListener() {
                    @Override
                    public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                        alarm.setRingtonePath(ringtoneUri.toString());
                        alarm.setRingtoneName(ringtoneName);
                        selectedRingtoneTextView.setText(ringtoneName);
                    }
                });

        //Add the desirable ringtone types.
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);

        //Display the dialog.
        ringtonePickerBuilder.show();
    }

    private String getFormattedTime(int hourOfDay, int minute) {

        String time = "";
        try {
            time = StringFormatter.getFormattedTimeDigits(hourOfDay) + " : " + StringFormatter.getFormattedTimeDigits(minute);

        } catch (Exception exception) {
            //log
            dialogInteractor.displayReactiveDialog("Time Conversion Failed", "Please retry!", null);
        }

        return time;
    }

    private View.OnClickListener getSaveAlarmClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AddAlarmActivity.class.getSimpleName(), String.valueOf(seebBarProgress));

                //TODO needs validation

                if (TextUtils.isEmpty(selectedRingtoneTextView.getText()) || selectedRingtoneTextView.getText().equals("None")) {
                    Toast.makeText(AddAlarmActivity.this, "Please select a Ringtone", Toast.LENGTH_SHORT).show();
                    return ;
                }

                alarmService.get(alarm.getHour(), alarm.getMinutes(), new OnAsyncResponse<List<Alarm>>() {
                    @Override
                    public void processResponse(List<Alarm> response) {
                        if(response == null || response.size() == 0){
                            startSavingProcess();
                        }
                        else{
                            if(response.size() == 1 && response.get(0).getId() == alarm.getId()){
                                startSavingProcess();
                            }
                            else {

                                try {
                                    Toast.makeText(activity, "You cannot create an alarm before/ after 30 minutes from " + StringFormatter.getFormattedTimeDigits(alarm.getHour()) + " : " + StringFormatter.getFormattedTimeDigits(alarm.getMinutes()) + ", since one already exists.", Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {
                                    Crashlytics.logException(ex);
                                }
                            }
                        }
                    }
                });
            }
        };
    }

    private void startSavingProcess() {
        alarm.setVobrateOn(vibrateSwitch.isChecked());
        alarm.setVolume(seebBarProgress);
        alarm.setTurnedOn(true);

        if (!TextUtils.isEmpty(titleView.getText())) {
            alarm.setTitle(titleView.getText().toString());
        }

        alarm.setRepeatDays(TextUtils.join(",", adapter.getClickedItemsList()));

        alarm.setSnoozeAmount(getSnoozeAmount());

        final long timeToStartAlarm = calculateTimeToStartAlarm(alarm.getRepeatDays());

        Toast.makeText(activity, TimeHelper.getTimeDifference(timeToStartAlarm), Toast.LENGTH_LONG).show();

        String debuglog = "Save alarm with " + alarm.getHour() + " H : " + alarm.getMinutes() + " M, with snooze of " + alarm.getSnoozeAmount() + " and repeat days: " + alarm.getRepeatDays();
        Crashlytics.log(99, AddAlarmActivity.class.getName(), debuglog);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, debuglog);
        firebaseAnalytics.logEvent("debuglog_addalarm", bundle);
        if (isEdit) {


            alarmService.update(alarm, new OnAsyncResponse<Long>() {
                @Override
                public void processResponse(Long response) {

                    alarmLaunchHandler.cancelAlarm(alarm.getId());

                    alarmLaunchHandler.registerAlarm(response.intValue(), timeToStartAlarm);

                    finish();
                }
            });
        } else {
            alarmService.add(alarm, new OnAsyncResponse<Long>() {
                @Override
                public void processResponse(Long response) {

                    alarmLaunchHandler.registerAlarm(response.intValue(), timeToStartAlarm);

                    displayAskForPhoneNoPopUp();
                }
            });
        }

        sharedPreferencesContainer.increaseSetAlarmsCount();

        startProcessingFailedPayments();
    }

    private long calculateTimeToStartAlarm(String repeatDays){
        long startRingingTime = 0;
        if(TextUtils.isEmpty(repeatDays)){//if no repeat days
            if(TimeHelper.isTimeInThePast(alarm.getHour(), alarm.getMinutes())){//if alarm is in the past => delay with 1 day
                startRingingTime = TimeHelper.getTimeInMillisecondsAndDelayWithDays(alarm.getHour(), alarm.getMinutes(), 1);
            }
            else{// if alarm is in the future return the time
                startRingingTime = TimeHelper.getTimeInMilliseconds(alarm.getHour(), alarm.getMinutes());
            }
        }
        else {//calculate based on the current day vs available repeat days
            int delayDays = TimeHelper.getDaysUntilRepeatDay(repeatDays);

            startRingingTime = TimeHelper.getTimeInMillisecondsAndDelayWithDays(alarm.getHour(), alarm.getMinutes(), delayDays);
        }
        return startRingingTime;
    }


    private void displayAskForPhoneNoPopUp() {
        if(this.sharedPreferencesContainer.getShowedAskForPhoneNoPopup()){
           finish();
        }
        else{
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.phoneno_popup);
            ImageView closePopUp = dialog.findViewById(R.id.closePhoneNoPopUpId);
            closePopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            TextView phoneNoText = dialog.findViewById(R.id.phoneNoTxtView);


            TextView saveBtn = dialog.findViewById(R.id.savePhoneNoBtn);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferencesContainer.setCurrentUserPhoneNumber(phoneNoText.getText().toString());
                    sharedPreferencesContainer.setShowedAskForPhoneNoPopup();
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
        }
    }

    private void startProcessingFailedPayments() {

        AppCompatActivity activityCompat = this;
        paymentDetailsRepository.getAll(PaymentStatus.NotConnectedToInternetToPay, new OnAsyncResponse<List<PaymentDetails>>() {
            @Override
            public void processResponse(List<PaymentDetails> response) {
                if (response != null && response.size() > 0) {

                    int[] alarmReactionIds = new int[response.size()];
                    int index = 0;
                    for (PaymentDetails paymentDetails : response) {
                        alarmReactionIds[index++] = paymentDetails.getAlarmReactionId();
                    }
                    Intent serviceIntent = new Intent(activityCompat, ProcessFailedPaymentsService.class);
                    serviceIntent.putExtra("AlarmReactionIds", alarmReactionIds);

                    ContextCompat.startForegroundService(activityCompat, serviceIntent);
                }
            }
        });
    }

    private int getSnoozeAmount() {

        try {
            int snoozeAmount = getSnoozeAmountFromItem(snoozeAdapter.selectedItem);
            return snoozeAmount;
        } catch (ClassCastException castException) {

            dialogInteractor.displayReactiveDialog("Snooze converions", "Snooze value was not selected!", null);
            return 5;//this will be the dafult value for testing, until we implement a screen for snoozes
        }
    }

    private int getSnoozeAmountFromItem(int position) {
        switch (position) {
            case -1:
                return 1;
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 15;
            case 4:
                return 30;
            default:
                return 1;
        }
    }

    private void createSnoozeDialog() {
        snoozeDialog = new Dialog(AddAlarmActivity.this);
        snoozeDialog.setContentView(R.layout.snooze_dialog);
        snoozeDialog.setCancelable(false);
        snoozeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = snoozeDialog.getWindow();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        window.setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        snoozeAdapter = new SnoozeAdapter(durations);
        snoozeRv = snoozeDialog.findViewById(R.id.snoozeRv);
        snoozeRv.setLayoutManager(new LinearLayoutManager(AddAlarmActivity.this));
        snoozeRv.setAdapter(snoozeAdapter);
        snoozeRv.setHasFixedSize(true);

        Button ok = (Button) snoozeDialog.findViewById(R.id.okButton);
        Button cancel = (Button) snoozeDialog.findViewById(R.id.cancelButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snoozeDialog.dismiss();
                if (getSnoozeAmountFromItem(snoozeAdapter.selectedItem) == 1) {
                    selectedSnooze.setText(String.valueOf(getSnoozeAmountFromItem(snoozeAdapter.selectedItem)) + " minute");
                } else if (getSnoozeAmountFromItem(snoozeAdapter.selectedItem) > 1) {
                    selectedSnooze.setText(String.valueOf(getSnoozeAmountFromItem(snoozeAdapter.selectedItem)) + " minutes");
                } else {
                    selectedSnooze.setText("");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snoozeDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                finish();
                break;
        }
    }
}