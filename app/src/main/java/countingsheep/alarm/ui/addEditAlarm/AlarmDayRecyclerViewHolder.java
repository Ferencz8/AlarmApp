package countingsheep.alarm.ui.addEditAlarm;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import countingsheep.alarm.R;

public class AlarmDayRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView textView = null;
    private ImageView dayImageView = null;
    private boolean isClicked = false;

    public AlarmDayRecyclerViewHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            textView = (TextView)itemView.findViewById(R.id.dayText);
            dayImageView = (ImageView) itemView.findViewById(R.id.dayImageView);
        }
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getDayImageView() {
        return dayImageView;
    }
}
