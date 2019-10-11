package countingsheep.alarm.ui.roasts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.Share;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.ShareHelper;
import countingsheep.alarm.ui.shared.EmptyAdapterDataObserver;

public class RoastHistoryFragment extends Fragment {
    protected FirebaseAnalytics firebaseAnalytics;
    private List<Message> roasts;
    private RecyclerView recyclerView;
    private RoastListRecyclerViewDataAdapter adapter;
    EmptyAdapterDataObserver dataObserver;
    private ShareHelper shareHelper;

    @Inject
    MessageService messageService;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    private static RoastHistoryFragment fragment;

    public static synchronized RoastHistoryFragment newInstance() {
        if (fragment == null) {
            fragment = new RoastHistoryFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getActivityComponent(getActivity()).inject(RoastHistoryFragment.this);

        roasts = new ArrayList<>();

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("roastHistory", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_roast_list, container, false);
        Log.e(RoastHistoryFragment.class.getName(), "onCreateView");
        recyclerView = view.findViewById(R.id.roastListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);

        shareHelper = new ShareHelper(getActivity());
        // Create recycler view data adapter with item list.
        adapter = new RoastListRecyclerViewDataAdapter(getActivity(), roasts, messageService, sharedPreferencesContainer, shareHelper);

        // Set data adapter.
        recyclerView.setAdapter(adapter);
        dataObserver = new EmptyAdapterDataObserver(adapter, view.findViewById(R.id.emptyList), recyclerView);
        adapter.registerAdapterDataObserver(dataObserver);

        initRoasts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.sharedPreferencesContainer.getUserInitiatedShare()) {
            this.sharedPreferencesContainer.setUserInitiatedShare(false);
            this.sharedPreferencesContainer.increaseFreeCredits(5);
            Toast.makeText(getContext(), "+ 5 Credits", Toast.LENGTH_LONG).show();
        }
        Log.e(RoastHistoryFragment.class.getName(), "onResume");
        initRoasts();
    }

    private void initRoasts() {
        Log.e(RoastHistoryFragment.class.getName(), "InitRoasts");
        messageService.getRoastMessageHistory(response -> {
            adapter.updateData(response);
            dataObserver.onChanged();
            recyclerView.scrollToPosition(1);
            Log.e(RoastHistoryFragment.class.getName(), "InitRoasts 2");
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ShareHelper.UserFinishedSharing) {
                //GIVE CREDITS
                ShareHelper.UserFinishedSharing = false;
                shareHelper.onSuccess(null);
            } else {
                shareHelper.onFailure();
            }
        } else {
            //CANCELED
            shareHelper.cancelReactionTimer();
        }
    }
}
