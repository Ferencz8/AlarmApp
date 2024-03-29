package countingsheep.alarm.ui.alarmList;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.protobuf.Empty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
import countingsheep.alarm.ui.addEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.ui.shared.EmptyAdapterDataObserver;
import retrofit2.Retrofit;

public class AlarmsFragment extends Fragment {
    protected FirebaseAnalytics firebaseAnalytics;
    RecyclerView recyclerView;

    AlarmListRecyclerViewDataAdapter adapter;
    EmptyAdapterDataObserver dataObserver;
    List<Alarm> alarms = new ArrayList<Alarm>();
    ImageView addAlarm;


    @Inject
    AlarmService alarmService;

    @Inject
    Retrofit retrofit;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    AlarmLaunchHandler alarmLaunchHandler;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Inject
    PaymentDetailsRepository paymentDetailsRepository;

    private static AlarmsFragment fragment;
    public static synchronized AlarmsFragment newInstance() {
        if(fragment==null) {
            fragment = new AlarmsFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        alarms = new ArrayList<>();
        super.onCreate(savedInstanceState);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("alarms_list",null);

        Injector.getActivityComponent(getActivity()).inject(AlarmsFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        Log.e(AlarmsFragment.class.getName(), "onCreateView");

        recyclerView = view.findViewById(R.id.alarmListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        // Set layout manager.
        recyclerView.setLayoutManager(mLayoutManager);

        // Create car recycler view data adapter with car item list.
        adapter = new AlarmListRecyclerViewDataAdapter(getActivity(), alarms, dialogInteractor, alarmLaunchHandler, alarmService);

        // Set data adapter.
        recyclerView.setAdapter(adapter);

        dataObserver = new EmptyAdapterDataObserver(adapter, view.findViewById(R.id.emptyList), recyclerView);
        adapter.registerAdapterDataObserver(dataObserver);


        initAlarms();
        // Scroll RecyclerView a little to make later scroll take effect.


        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        addAlarm = (ImageView) view.findViewById(R.id.addAlarmButtonId);
        addAlarm.setOnClickListener(getAddAlarmOnClick());

        return view;
    }

    private View.OnClickListener getAddAlarmOnClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!sharedPreferencesContainer.getEternalCredits() && sharedPreferencesContainer.getFreeCredits() == 0){
//                else if(sharedPreferencesContainer.getFreeCredits() == 0 && !sharedPreferencesContainer.doesAllPaymentInformationExist()){

                    dialogInteractor.displayInfoDialog(R.drawable.ic_icon_alert_layer, "You have no credits left. Add more in order to add an alarm.");
                }
                else{
                    Intent intent = new Intent(getView().getContext(), AddAlarmActivity.class);
                    startActivity(intent);
                }

//                    paymentDetailsRepository.getAll(PaymentStatus.Failed, new OnAsyncResponse<List<PaymentDetails>>() {
//                        @Override
//                        public void processResponse(List<PaymentDetails> response) {
//                            //TODO: add loading bar
//                            //TODO:: if slow then create a flag with existing failed payment statuses in shared pref, once the updatePaymentStatusJob runs and
//                            //only then use this logic
//                            if(response!=null && response.size() > 0){
//
//                                dialogInteractor.displayInfoDialog(R.drawable.ic_icon_clock, "First please pay the failed transactions!", () -> {
//
////                                    Intent intent = new Intent(getView().getContext(), AlarmHistoryActivity.class);
////                                    startActivity(intent);
//                                });
//                            }
//                            else {
//
//                                Intent intent = new Intent(getView().getContext(), AddAlarmActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }
            }
        };
    }

    private void initAlarms() {
        Log.e(AlarmsFragment.class.getName(), "initAlarms");
        alarmService.getAll(response -> {
            adapter.updateData(response);
            dataObserver.onChanged();
            recyclerView.scrollToPosition(1);
            Log.e(AlarmsFragment.class.getName(), "initAlarms 2");
        });
    }

    @Override
    public void onResume(){

        Log.e(AlarmsFragment.class.getName(), "onResume");
        initAlarms();
        super.onResume();
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.TRANSPARENT);
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.trash_icon);
                xMark.setColorFilter(getResources().getColor(R.color.colorPrimary, null), PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.fab_margin);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();

                showDeleteAlarmDialog(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showDeleteAlarmDialog(int swipedPosition) {
        this.dialogInteractor.displayReactiveDialog("Delete Alarm",
                "Are you sure you want to delete this alarm?", new DialogInteractor.OnReaction() {
                    @Override
                    public void onPositive() {
                        Alarm alarm = adapter.getAlarm(swipedPosition);
                        alarmLaunchHandler.cancelAlarm(alarm.getId());
                        alarmService.delete(alarm.getId());
                        adapter.remove(swipedPosition);
                    }

                    @Override
                    public void onNegative() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                if (parent.getItemAnimator().isRunning()) {

                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    int left = 0;
                    int right = parent.getWidth();

                    int top = 0;
                    int bottom = 0;

                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }
}
