package countingsheep.alarm.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.core.domain.AlarmModel;

public class AlarmListRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmListRecyclerViewHolder> {

    private List<AlarmModel> viewItemList;

    public AlarmListRecyclerViewDataAdapter(List<AlarmModel> viewItemList) {
        this.viewItemList = viewItemList;
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
            AlarmModel viewItem = viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                holder.getTitleView().setText(viewItem.title);
                holder.getHourView().setText(viewItem.minutes + ":" + viewItem.seconds);
                holder.getRepeatDaysView().setText("Default");
                holder.getOnBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_on);
                holder.getOffBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_off);

                if (viewItem.isTurnedOn){
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                    holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                }
                else {
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                    holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                }

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