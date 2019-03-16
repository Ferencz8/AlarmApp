package countingsheep.alarm.ui.addEditAlarm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class AddAlarmActivity extends AppCompatActivity {

    private Alarm alarm;

    AlarmDayRecyclerViewDataAdapter adapter;
    private List<AlarmDayRecyclerViewItem> daysList = null;
    private List<String> weekDays = Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su");
    private Switch vibrateSwitch;
    private SeekBar volumeSeekBar;
    private ImageView saveImageView;
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

        bindViews();

        setupAlarm();
    }

    private void setupAlarm(){
        alarm = getAlarmFromIntent();
        if (alarm != null) {

                isEdit = true;
                String time  = this.getFormattedTime(alarm.getHour(), alarm.getMinutes());
                timeView.setText(time);
                titleView.setText(alarm.getTitle());
                vibrateSwitch.setChecked(alarm.isVobrateOn());
                volumeSeekBar.setProgress(alarm.getVolume());

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
                AlarmDayRecyclerViewItem item = new AlarmDayRecyclerViewItem();
                item.setText(day);
                daysList.add(item);
            }
        }

        saveImageView = findViewById(R.id.saveAlarmAddImageView);
        saveImageView.setOnClickListener(getSaveAlarmClickListener());
    }

    private String getFormattedTime(int hourOfDay, int minute){

        String time = "";
        try {
            time = StringFormatter.getFormattedTimeDigits(hourOfDay) + " : " + StringFormatter.getFormattedTimeDigits(minute);

        }catch(Exception exception){
            //log
            dialogInteractor.displayDialog("Time Conversion Failed", "Please retry!", null);
        }

        return time;
    }

    private View.OnClickListener getSaveAlarmClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AddAlarmActivity.class.getSimpleName(), String.valueOf(seebBarProgress));

                //TODO needs validation

                alarm.setVolume(seebBarProgress);
                alarm.setTurnedOn(true);

                if (!TextUtils.isEmpty(titleView.getText())) {
                    alarm.setTitle(titleView.getText().toString());
                }


                //This is for testing

                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                calendar.set(Calendar.MINUTE, alarm.getMinutes());
                calendar.set(Calendar.SECOND, 0);

                if (isEdit) {
                    alarmService.update(alarm);

                    alarmLaunchHandler.cancelAlarm(alarm.getId());

                    alarmLaunchHandler.registerAlarm(alarm.getId(), calendar.getTimeInMillis());
                } else {
                    alarmService.add(alarm, new OnAsyncResponse<Long>() {
                        @Override
                        public void processResponse(Long response) {

                            alarmLaunchHandler.registerAlarm(response.intValue(), calendar.getTimeInMillis());
                        }
                    });
                }

                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }
}