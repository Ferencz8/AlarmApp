package countingsheep.alarm.ui.addEditAlarm;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.StringFormatter;
import countingsheep.alarm.util.TimeHelper;

public class AddAlarmActivity extends AppCompatActivity {

    private Alarm alarm;

    AlarmDayRecyclerViewDataAdapter adapter;
    private List<AlarmDayRecyclerViewItem> daysList = null;
    private List<String> weekDays = Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su");
    private Switch vibrateSwitch;
    private SeekBar volumeSeekBar;
    private ImageView saveImageView;
    private TextView selectedRingtoneTextView;
    private TextView timeView;
    private Spinner snoozeSpinner;
    private Integer snoozes[] = {1, 5, 10, 15, 30};
    private EditText titleView;
    private boolean isEdit = false;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    DialogInteractor dialogInteractor;

    private int seebBarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Injector.getActivityComponent(this).inject(this);

        setupAlarm();

        bindViews();
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
        adapter = new AlarmDayRecyclerViewDataAdapter(daysList);
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
                }
                else{
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
        snoozeSpinner = findViewById(R.id.snoozeSpinnerId);
        ArrayAdapter<Integer> snoozesAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, snoozes);
        //snoozesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        snoozeSpinner.setAdapter(snoozesAdapter);
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

        saveImageView = findViewById(R.id.saveAlarmAddImageView);
        saveImageView.setOnClickListener(getSaveAlarmClickListener());

        String time;
        if(isEdit){
            time = this.getFormattedTime(alarm.getHour(), alarm.getMinutes());

            selectedRingtoneTextView.setText(alarm.getRingtoneName());

            adapter.markDaysAsSelected(Arrays.asList(alarm.getRepeatDays().split(",")));

            vibrateSwitch.setChecked(alarm.isVobrateOn());
        }
        else{
            time = this.getFormattedTime(7, 5);
        }
        timeView.setText(time);
        titleView.setText(alarm.getTitle());
        vibrateSwitch.setChecked(alarm.isVobrateOn());
        volumeSeekBar.setProgress(alarm.getVolume());
    }

    private AlarmDayRecyclerViewItem getAdarmDayViewItem(String day){
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
                    this.dialogInteractor.displayDialog("Permission Impact", "The permission needs to be granted in order for the app to be allowed to show ringtones", null);
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
            dialogInteractor.displayDialog("Time Conversion Failed", "Please retry!", null);
        }

        return time;
    }

    private View.OnClickListener getSaveAlarmClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AddAlarmActivity.class.getSimpleName(), String.valueOf(seebBarProgress));

                //TODO needs validation

                if(TextUtils.isEmpty(selectedRingtoneTextView.getText()) || selectedRingtoneTextView.getText().equals("None")){
                    Toast.makeText(AddAlarmActivity.this, "Please select a Ringtone", Toast.LENGTH_SHORT).show();
                    return;
                }


                alarm.setVobrateOn(vibrateSwitch.isChecked());
                alarm.setVolume(seebBarProgress);
                alarm.setTurnedOn(true);

                if (!TextUtils.isEmpty(titleView.getText())) {
                    alarm.setTitle(titleView.getText().toString());
                }

                alarm.setRepeatDays(TextUtils.join(",", adapter.getClickedItemsList()));

                alarm.setSnoozeAmount(getSnoozeAmount());

                final long timeToStartAlarm = TimeHelper.getTimeInMilliseconds(alarm.getHour(), alarm.getMinutes());

                if (isEdit) {
                    alarmService.update(alarm);

                    alarmLaunchHandler.cancelAlarm(alarm.getId());

                    alarmLaunchHandler.registerAlarm(alarm.getId(), timeToStartAlarm);
                } else {
                    alarmService.add(alarm, new OnAsyncResponse<Long>() {
                        @Override
                        public void processResponse(Long response) {

                            alarmLaunchHandler.registerAlarm(response.intValue(), timeToStartAlarm);
                        }
                    });
                }

                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }

    private int getSnoozeAmount() {

        try {
            int snoozeAmount = (int) snoozeSpinner.getSelectedItem();
            return snoozeAmount;
        } catch (ClassCastException castException) {

            dialogInteractor.displayDialog("Snooze converions", "Snooze value was not selected!", null);
            return 5;//this will be the dafult value for testing, until we implement a screen for snoozes
        }
    }
}