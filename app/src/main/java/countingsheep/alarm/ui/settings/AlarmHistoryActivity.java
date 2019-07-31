package countingsheep.alarm.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.adapters.AlarmHistoryRecyclerAdapter;
import countingsheep.alarm.ui.settings.models.AlarmHistory;

public class AlarmHistoryActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AlarmHistoryRecyclerAdapter adapter;
    private ArrayList<AlarmHistory> alarms;
    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    @Inject
    AlarmReactionService alarmReactionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_history);

        Injector.getActivityComponent(this).inject(this);

        bindViews();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);
        alarms = new ArrayList<>();
        alarmReactionService.getAllAlarmReactionHistory(new OnAsyncResponse<List<AlarmHistory>>() {
            @Override
            public void processResponse(List<AlarmHistory> response) {
                alarms.addAll(response);
                synchronized(adapter) {
                    adapter.notify();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter = new AlarmHistoryRecyclerAdapter(this,alarms );
        recyclerView.setAdapter(adapter);
    }

    private void bindViews(){
        recyclerView = findViewById(R.id.alarm_history_rv);
        headerBar = findViewById(R.id.title_bar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        titleTv.setText(R.string.alarm_history);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                finish();
                break;
        }

    }
}
