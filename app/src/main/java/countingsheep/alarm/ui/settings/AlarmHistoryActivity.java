package countingsheep.alarm.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import countingsheep.alarm.R;
import countingsheep.alarm.ui.adapters.AlarmHistoryRecyclerAdapter;

public class AlarmHistoryActivity extends FragmentActivity implements View.OnClickListener {

    private RecyclerView recyclerView;;
    private AlarmHistoryRecyclerAdapter adapter;
    private ArrayList<String> alarms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_history);

        recyclerView = findViewById(R.id.alarm_history_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);
        alarms = new ArrayList<>();
        alarms.add("hello");

        adapter = new AlarmHistoryRecyclerAdapter(this,alarms );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
