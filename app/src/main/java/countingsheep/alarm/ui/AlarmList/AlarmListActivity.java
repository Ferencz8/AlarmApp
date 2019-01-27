package countingsheep.alarm.ui.AlarmList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.core.services.interfaces.AlarmService;

public class AlarmListActivity extends AppCompatActivity
{
    AlarmListRecyclerViewDataAdapter adapter;
    List<AlarmModel> alarms;

    @Inject
    AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        setContentView(R.layout.content_alarm_list_item);

        RecyclerView recyclerView = findViewById(R.id.alarmListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);



        // Create car recycler view data adapter with car item list.
        adapter = new AlarmListRecyclerViewDataAdapter(this, alarms);
        // Set data adapter.
        recyclerView.setAdapter(adapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerView.scrollToPosition(1);

        List<AlarmModel> alarms = alarmService.getAll();
    }
}
