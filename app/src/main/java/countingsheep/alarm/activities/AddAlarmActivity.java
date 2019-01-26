package countingsheep.alarm.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.datainterfaces.AlarmRepository;
import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.dataaccess.AlarmDatabase;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.dataaccess.repositories.AlarmRepositoryImpl;
import countingsheep.alarm.internaldi.components.ActivityComponent;
import countingsheep.alarm.internaldi.modules.ActivityModule;

public class AddAlarmActivity extends AppCompatActivity {

    AlarmDayRecyclerViewDataAdapter adapter;
    private List<AlarmDayRecyclerViewItem> daysList = null;
    private List<String> weekDays = Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su");
    private Switch vibrateSwitch;
    private SeekBar volumeSeekBar;
    private ImageView saveImageView;
    private Spinner snoozeSpinner;
    private Integer snoozes[] = {1, 5, 10, 15, 30};

    @Inject
    AlarmRepository alarmRepository;



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

        //AlarmDatabase alarmDatabase = Room.databaseBuilder(this, AlarmDatabase.class, "db_countingSheep").build();
        //alarmRepository = new AlarmRepositoryImpl(alarmDatabase);
    }



    private void bindViews() {

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

                AlarmModel alarm = new AlarmModel();
                alarm.volume = seebBarProgress;
                alarm.minutes=15;
                alarm.seconds=22;
                alarm.isTurnedOn = true;
                alarm.title = "Test";

                Calendar calendar = Calendar.getInstance();
                //SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                //alarm.setCreatedAt(calendar.getTime());
                alarmRepository.insert(alarm);

                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}