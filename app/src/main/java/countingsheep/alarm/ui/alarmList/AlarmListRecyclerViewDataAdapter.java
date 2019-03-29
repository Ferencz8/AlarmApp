package countingsheep.alarm.ui.alarmList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.addEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.StringFormatter;

public class AlarmListRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmListRecyclerViewHolder> {

    private List<Alarm> viewItemList;
    private Activity activity;
    private DialogInteractor dialogInteractor;

    public AlarmListRecyclerViewDataAdapter(Activity activity, List<Alarm> viewItemList,
                                            DialogInteractor dialogInteractor) {
        this.viewItemList = viewItemList;
        this.activity = activity;
        this.dialogInteractor = dialogInteractor;
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
        if(viewItemList!=null) {
            // Get car item dto in list.
            final Alarm viewItem = viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                holder.getTitleView().setText(viewItem.getTitle());
                holder.getHourView().setText(getFormattedTime(viewItem.getHour(),viewItem.getMinutes()));
                holder.getRepeatDaysView().setText(viewItem.getRepeatDays());
                holder.getAlarmLayout().setBackgroundColor(getColorWithAlpha(R.color.colorTransparentWhite, 0.7f));
                //holder.getOnBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_on);
                //holder.getOffBackgroundImageView().setImageResource(R.drawable.ic_alarms_rectangle_alarm_off);

                if (viewItem.isTurnedOn()){
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                    //holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                }
                else {
                    holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                    //holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
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

                //holder.getOnBackgroundImageView().setOnClickListener(onClickListener);
                //holder.getOffBackgroundImageView().setOnClickListener(onClickListener);

                holder.getOnOffImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()){
                            holder.setClicked(false);

                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepoff);
                            holder.getAlarmLayout().setBackgroundColor(getColorWithAlpha(R.color.colorTransparentWhite, 0.9f));
                            //holder.getOffBackgroundImageView().setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.setClicked(true);
                            holder.getOnOffImageView().setImageResource(R.drawable.ic_sheepon);
                            holder.getAlarmLayout().setBackgroundColor(getColorWithAlpha(R.color.colorTransparentWhite, 0.7f));
                            //holder.getOffBackgroundImageView().setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }
    }

    private String getFormattedTime(int hourOfDay, int minute){

        String time = "";
        try {
            time = StringFormatter.getFormattedTimeDigits(hourOfDay) + " : " + StringFormatter.getFormattedTimeDigits(minute);

        }catch(Exception exception){
            //log
            dialogInteractor.displayDialog("Time Conversion Failed", "Please retry!", null);
        }

        return time;
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

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}