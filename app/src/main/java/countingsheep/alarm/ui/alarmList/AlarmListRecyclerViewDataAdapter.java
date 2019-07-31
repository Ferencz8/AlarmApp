package countingsheep.alarm.ui.alarmList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.addEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.StringFormatter;
import countingsheep.alarm.util.TimeHelper;


public class AlarmListRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmListRecyclerViewHolder> {

    private List<Alarm> viewItemList;
    private Activity activity;
    private DialogInteractor dialogInteractor;
    private AlarmLaunchHandler alarmLaunchHandler;
    private AlarmService alarmService;

    public AlarmListRecyclerViewDataAdapter(Activity activity, List<Alarm> viewItemList,
                                            DialogInteractor dialogInteractor, AlarmLaunchHandler alarmLaunchHandler,
                                            AlarmService alarmService) {
        this.viewItemList = viewItemList;
        this.activity = activity;
        this.dialogInteractor = dialogInteractor;
        this.alarmLaunchHandler = alarmLaunchHandler;
        this.alarmService = alarmService;
    }

    @Override
    public AlarmListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.content_alarm_list_new_item, parent, false);

        // Create and return our customRecycler View Holder object.
        AlarmListRecyclerViewHolder ret = new AlarmListRecyclerViewHolder(itemView);
        return ret;
    }


    @Override
    public void onBindViewHolder(final AlarmListRecyclerViewHolder holder, int position) {
        if (viewItemList != null) {
            // Get car item dto in list.
            final Alarm viewItem = viewItemList.get(position);

            if (viewItem != null) {
                // Set car item title.
                holder.getTitleView().setText(viewItem.getTitle());
                holder.getHourView().setText(getFormattedTime(viewItem.getHour(), viewItem.getMinutes()));
                holder.getRepeatDaysView().setText(viewItem.getRepeatDays());
                holder.getOverlay().setVisibility(View.GONE);
                holder.getAlarmLayout().setBackgroundColor(activity.getResources().getColor(R.color.colorSecondary, null));
                //holder.getOnBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_on);
                //holder.getOffBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_off);

                if (viewItem.isTurnedOn()) {
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                    //holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                } else {
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                    //holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                }

                View.OnClickListener onClickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, AddAlarmActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("alarm", viewItem);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                };

                holder.itemView.setOnClickListener(onClickListener);
                //holder.getOnBackgroundImageView().setOnClickListener(onClickListener);
                //holder.getOffBackgroundImageView().setOnClickListener(onClickListener);

                holder.getOnOffImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()) {
                            holder.setClicked(false);

                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                            holder.getOverlay().setVisibility(View.VISIBLE);
//                            holder.getAlarmLayout().setBackgroundColor(getColorWithAlpha(R.color.colorSecondary, 0.9f));

                            changeAlarmState(false, viewItem.getId(), 0);
                            //holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                            alarmService.switchOnOf(viewItem.getId(),false);
                        }
                        else {
                            holder.setClicked(true);
                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                            holder.getOverlay().setVisibility(View.GONE);
                            holder.getAlarmLayout().setBackgroundColor(activity.getResources().getColor(R.color.colorSecondary, null));

                            changeAlarmState(true, viewItem.getId(), TimeHelper.getTimeInMilliseconds(viewItem.getHour(), viewItem.getMinutes()));
                            //holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                            alarmService.switchOnOf(viewItem.getId(),true);
                        }
                    }
                });
            }
        }
    }

    private String getFormattedTime(int hourOfDay, int minute) {

        String time = "";
        try {
            time = StringFormatter.getFormattedTimeDigits(hourOfDay) + " : " + StringFormatter.getFormattedTimeDigits(minute);

        } catch (Exception exception) {
            Crashlytics.logException(exception);
            dialogInteractor.displayReactiveDialog("Time Conversion Failed", "Please retry!", null);
        }

        return time;
    }

    /**
     * Used to turn on/off the alarm. If it is on the alarm is registered, otherwise it gets canceled.
     *
     * @param state
     */
    private void changeAlarmState(boolean state, int alarmId, long triggerAtMillis) {
        if (state) {
            this.alarmLaunchHandler.registerAlarm(alarmId, triggerAtMillis);
        } else {
            this.alarmLaunchHandler.cancelAlarm(alarmId);
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (viewItemList != null) {
            ret = viewItemList.size();
        }
        return ret;
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public void remove(int position) {
        Alarm item = getAlarm(position);
        if (viewItemList.contains(item)) {
            viewItemList.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    public Alarm getAlarm(int position) {
        return viewItemList.get(position);
    }

    public void updateData(List<Alarm> alarms) {
        this.viewItemList.clear();
        this.viewItemList.addAll(alarms);
        this.notifyDataSetChanged();
    }
}