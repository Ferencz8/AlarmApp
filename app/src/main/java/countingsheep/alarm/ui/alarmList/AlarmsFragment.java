package countingsheep.alarm.ui.alarmList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.addEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.shared.DialogInteractor;
import retrofit2.Retrofit;

public class AlarmsFragment extends Fragment {

    RecyclerView recyclerView;

    AlarmListRecyclerViewDataAdapter adapter;
    List<Alarm> alarms;
    ImageView addAlarm;


    @Inject
    AlarmService alarmService;

    @Inject
    Retrofit retrofit;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    public static AlarmsFragment newInstance() {
        AlarmsFragment fragment = new AlarmsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        alarms = new ArrayList<>();
        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(getActivity()).inject(AlarmsFragment.this);

        initAlarms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        recyclerView = view.findViewById(R.id.alarmListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);

        // Create car recycler view data adapter with car item list.
        adapter = new AlarmListRecyclerViewDataAdapter(getActivity(), alarms, dialogInteractor, alarmLaunchHandler);

        // Set data adapter.
        recyclerView.setAdapter(adapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerView.scrollToPosition(1);

        addAlarm = (ImageView) view.findViewById(R.id.addAlarmButtonId);
        addAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getView().getContext(), AddAlarmActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initAlarms(){

        alarmService.getAll(new OnAsyncResponse<List<Alarm>>() {
            @Override
            public void processResponse(List<Alarm> response) {
                alarms.addAll(response);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
