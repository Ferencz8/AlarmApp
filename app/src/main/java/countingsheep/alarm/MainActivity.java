package countingsheep.alarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.R;
import countingsheep.alarm.activities.AddAlarmActivity;
import countingsheep.alarm.activities.AlarmListRecyclerViewDataAdapter;
import countingsheep.alarm.activities.LoginActivity;
import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.core.services.interfaces.AlarmService;

public class MainActivity extends AppCompatActivity
{
    AlarmListRecyclerViewDataAdapter adapter;
    List<AlarmModel> alarms;
    ImageView addAlarm;

    @Inject
    AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarms = new ArrayList<>();
        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.alarmListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                alarms.addAll(alarmService.getAll());

                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {

                adapter.notifyDataSetChanged();
            }

        }.execute();

        // Create car recycler view data adapter with car item list.
        adapter = new AlarmListRecyclerViewDataAdapter(alarms);
        // Set data adapter.
        recyclerView.setAdapter(adapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerView.scrollToPosition(1);
        addAlarm = (ImageView) findViewById(R.id.addAlarmButtonId);
        addAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                startActivity(intent);
            }
        });
    }
}
