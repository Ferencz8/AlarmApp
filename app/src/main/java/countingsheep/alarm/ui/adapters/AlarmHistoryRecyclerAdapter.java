package countingsheep.alarm.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import countingsheep.alarm.R;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.settings.models.AlarmHistory;

public class AlarmHistoryRecyclerAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private ArrayList<AlarmHistory> alarms;

    public AlarmHistoryRecyclerAdapter(Activity activity, ArrayList<AlarmHistory> viewItemList) {
        this.alarms = viewItemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.item_alarm_history, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlarmHistory data = alarms.get(position);

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if(data.getName() == null || data.getName().isEmpty()) {
            itemViewHolder.alarmName.setVisibility(View.GONE);
        } else {
            itemViewHolder.alarmName.setVisibility(View.VISIBLE);
            itemViewHolder.alarmName.setText(data.getName());
        }
        itemViewHolder.alarmDate.setText(data.getCreatedDate());
        itemViewHolder.alarmHour.setText(data.getCreatedHour());
        itemViewHolder.alarmStatus.setText(data.getReactionType());
        itemViewHolder.cashSpend.setText(String.valueOf(data.getCashSpent())+" $");
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView alarmName;
        private TextView alarmDate;
        private TextView alarmHour;
        private TextView cashSpend;
        private TextView alarmStatus;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmName = itemView.findViewById(R.id.alarm_name);
            alarmDate = itemView.findViewById(R.id.alarm_date);
            alarmHour = itemView.findViewById(R.id.alarm_hour);
            cashSpend = itemView.findViewById(R.id.cash_spend);
            alarmStatus = itemView.findViewById(R.id.alarm_status);
        }
    }
}
