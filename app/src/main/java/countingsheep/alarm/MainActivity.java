package countingsheep.alarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.dataaccess.apiinterfaces.AlarmAPI;
import countingsheep.alarm.dataaccess.apiinterfaces.UserWrappedEntity;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.ui.AddEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.AlarmList.AlarmListRecyclerViewDataAdapter;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
{
    AlarmListRecyclerViewDataAdapter adapter;
    List<Alarm> alarms;
    ImageView addAlarm;

    @Inject
    AlarmService alarmService;

    @Inject
    Retrofit retrofit;

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
        adapter = new AlarmListRecyclerViewDataAdapter(this, alarms);
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
