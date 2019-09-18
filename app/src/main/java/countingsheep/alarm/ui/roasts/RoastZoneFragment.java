package countingsheep.alarm.ui.roasts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;

import static countingsheep.alarm.db.entities.RoastZoneItem.OPT_BATTLE_FIELD;
import static countingsheep.alarm.db.entities.RoastZoneItem.OPT_LEADERBOARD;
import static countingsheep.alarm.db.entities.RoastZoneItem.OPT_ROAST_CHAT;
import static countingsheep.alarm.db.entities.RoastZoneItem.OPT_ROAST_FRIEND;
import static countingsheep.alarm.db.entities.RoastZoneItem.OPT_YOUR_ROASTS;

public class RoastZoneFragment extends Fragment implements RoastZoneRecyclerViewAdapter.RoastZoneListener {
    protected FirebaseAnalytics firebaseAnalytics;
    private RecyclerView recyclerView;
    private RoastZoneRecyclerViewAdapter adapter;

    private static RoastZoneFragment fragment;
    public static synchronized RoastZoneFragment newInstance() {
        if(fragment==null) {
            fragment = new RoastZoneFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getActivityComponent(getActivity()).inject(RoastZoneFragment.this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("roastZone",null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roast_list, container, false);

        recyclerView = view.findViewById(R.id.roastListRecyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RoastZoneRecyclerViewAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case OPT_YOUR_ROASTS:
                MainActivity act = (MainActivity) getActivity();
                if (act != null) act.setFragment(RoastHistoryFragment.newInstance(), true);
                break;
            case OPT_ROAST_FRIEND:
                showLocked();
                break;
            case OPT_LEADERBOARD:
                showLocked();
                break;
            case OPT_ROAST_CHAT:
                showLocked();
                break;
            case OPT_BATTLE_FIELD:
                showLocked();
                break;
        }
    }

    private void showLocked() {
        new LockedFeatureDialog(requireContext()).show();
    }
}
