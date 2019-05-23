package countingsheep.alarm.ui.alarmList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import countingsheep.alarm.R;

public class AlarmListRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView = null;
    private ImageView onOffImageView = null;
    //private ImageView offBackgroundImageView = null;
   // private ImageView onBackgroundImageView = null;
    private TextView repeatDaysView = null;
    private TextView timeView = null;
    private ConstraintLayout alarmLayout = null;

    private boolean isClicked = true;

    public AlarmListRecyclerViewHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            titleView = (TextView)itemView.findViewById(R.id.alarmListTitleId);
            onOffImageView = (ImageView) itemView.findViewById(R.id.alarmListOnOffImageId);
            repeatDaysView = (TextView)itemView.findViewById(R.id.alarmListTimeRepeatId);
            timeView = (TextView)itemView.findViewById(R.id.alarmListTimeId);
            alarmLayout = (ConstraintLayout) itemView.findViewById(R.id.alarmItemLayout);
            //offBackgroundImageView = (ImageView)itemView.findViewById(R.id.alarmListOffBackgroundmageViewId);
            //onBackgroundImageView = (ImageView)itemView.findViewById(R.id.alarmListOnBackgroundmageViewId);
        }
    }

//    public ImageView getOnBackgroundImageView() {
//        return onBackgroundImageView;
//    }
//
//    public void setOnBackgroundImageView(ImageView onBackgroundImageView) {
//        this.onBackgroundImageView = onBackgroundImageView;
//    }
//
//    public void setOffBackgroundImageView(ImageView offBackgroundImageView) {
//        this.offBackgroundImageView = offBackgroundImageView;
//    }
//
//    public ImageView getOffBackgroundImageView() {
//        return offBackgroundImageView;
//    }

    public ConstraintLayout getAlarmLayout() {
        return alarmLayout;
    }

    public void setAlarmLayout(ConstraintLayout alarmLayout) {
        this.alarmLayout = alarmLayout;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleView(TextView titleView) {
        this.titleView = titleView;
    }

    public ImageView getOnOffImageView() {
        return onOffImageView;
    }

    public void setOnOffImageView(ImageView onOffImageView) {
        this.onOffImageView = onOffImageView;
    }

    public TextView getRepeatDaysView() {
        return repeatDaysView;
    }

    public void setRepeatDaysView(TextView repeatDaysView) {
        this.repeatDaysView = repeatDaysView;
    }

    public TextView getHourView() {
        return timeView;
    }

    public void setHourView(TextView hourView) {
        this.timeView = hourView;
    }
}
