package countingsheep.alarm.ui.AlarmList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.ui.AddEditAlarm.AddAlarmActivity;

public class AlarmListRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmListRecyclerViewHolder> {

    private List<Alarm> viewItemList;
    private Activity activity;

    public AlarmListRecyclerViewDataAdapter(Activity activity, List<Alarm> viewItemList) {
        this.viewItemList = viewItemList;
        this.activity = activity;
    }

    @Override
    public AlarmListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.content_alarm_list_item, parent, false);

        // Create and return our customRecycler View Holder object.
        AlarmListRecyclerViewHolder ret = new AlarmListRecyclerViewHolder(itemView);
        return ret;
    }



    @Override
    public void onBindViewHolder(final AlarmListRecyclerViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get car item dto in list.
            final Alarm viewItem = viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                holder.getTitleView().setText(viewItem.getTitle());
                holder.getHourView().setText(viewItem.getHour() + ":" + viewItem.getMinutes());
                holder.getRepeatDaysView().setText("Default");
                holder.getOnBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_on);
                holder.getOffBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_off);

                if (viewItem.isTurnedOn()){
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                    holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                }
                else {
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                    holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                }

                View.OnClickListener onClickListener = new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, AddAlarmActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("alarm", viewItem);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                };

                holder.getOnBackgroundImageView().setOnClickListener(onClickListener);
                holder.getOffBackgroundImageView().setOnClickListener(onClickListener);

                holder.getOnOffImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()){
                            holder.setClicked(false);

                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                            holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.setClicked(true);
                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                            holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }
}