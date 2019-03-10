package countingsheep.alarm.ui.addEditAlarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;

public class AddAlarmActivity extends AppCompatActivity {

    AlarmDayRecyclerViewDataAdapter adapter;
    private List<AlarmDayRecyclerViewItem> daysList = null;
    private List<String> weekDays = Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su");
    private Switch vibrateSwitch;
    private SeekBar volumeSeekBar;
    private ImageView saveImageView;
    private Spinner snoozeSpinner;
    private Integer snoozes[] = {1, 5, 10, 15, 30};
    private EditText hourView;
    private EditText minView;
    private EditText titleView;
    private boolean isEdit = false;

    @Inject
    AlarmService alarmService;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    private int seebBarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getActivityComponent(this).inject(this);
        setContentView(R.layout.activity_add_alarm);

        RecyclerView recyclerView = findViewById(R.id.daysRecyclerView);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager layoutManager = new GridLayoutManager(this,7);

        // Set layout manager.
        recyclerView.setLayoutManager(layoutManager);
        bindViews();
        // Create car recycler view data adapter with car item list.
        adapter = new AlarmDayRecyclerViewDataAdapter(daysList);
        // Set data adapter.
        recyclerView.setAdapter(adapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerView.scrollToPosition(1);

        if(savedInstanceState!=null){
            Alarm alarm = (Alarm) savedInstanceState.getSerializable("alarm");
            if(alarm!=null){
                isEdit = true;
                hourView.setText(alarm.getHour());
                minView.setText(alarm.getMinutes());
                titleView.setText(alarm.getTitle());
                vibrateSwitch.setChecked(alarm.isVobrateOn());
                volumeSeekBar.setProgress(alarm.getVolume());
            }
        }
    }



    private void bindViews() {

        hourView = findViewById(R.id.hourEditTextId);
        minView = findViewById(R.id.minEditTextId);
        titleView = findViewById(R.id.titleEditTextId);
        vibrateSwitch = (Switch)findViewById(R.id.vibrateSwitchId);
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
        if(daysList == null)
        {
            daysList = new ArrayList<>();
            for (String day: weekDays) {
                AlarmDayRecyclerViewItem item = new AlarmDayRecyclerViewItem();
                item.setText(day);
                daysList.add(item);
            }
        }

        saveImageView = findViewById(R.id.saveAlarmAddImageView);
        saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AddAlarmActivity.class.getSimpleName(), String.valueOf( seebBarProgress));

                //TODO needs validation
                Alarm alarm = new Alarm();
                alarm.setVolume(seebBarProgress);
                alarm.setHour(Integer.parseInt(hourView.getText().toString()));
                alarm.setMinutes(Integer.parseInt(minView.getText().toString()));
                alarm.setTurnedOn(true);
                alarm.setTitle(titleView.getText().toString());


                if(isEdit){
                    alarmService.update(alarm);
                }
                else{
                    alarmService.add(alarm);
                }

                //This is for testing

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                calendar.set(Calendar.MINUTE, alarm.getMinutes());


                alarmLaunchHandler.registerAlarm(calendar.getTimeInMillis());

                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}