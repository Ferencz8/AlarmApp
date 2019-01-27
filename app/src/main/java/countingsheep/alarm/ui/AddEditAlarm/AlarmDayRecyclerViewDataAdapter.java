package countingsheep.alarm.ui.AddEditAlarm;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import countingsheep.alarm.R;

public class AlarmDayRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmDayRecyclerViewHolder> {

    private List<AlarmDayRecyclerViewItem> viewItemList;

    public AlarmDayRecyclerViewDataAdapter(List<AlarmDayRecyclerViewItem> viewItemList) {
        this.viewItemList = viewItemList;
    }

    @Override
    public AlarmDayRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.content_add_alarm_day, parent, false);

        // Create and return our customRecycler View Holder object.
        AlarmDayRecyclerViewHolder ret = new AlarmDayRecyclerViewHolder(itemView);
        return ret;
    }



    @Override
    public void onBindViewHolder(final AlarmDayRecyclerViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get car item dto in list.
            AlarmDayRecyclerViewItem viewItem = viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                holder.getTextView().setText(viewItem.getText());
                holder.getDayImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()){
                            holder.setClicked(false);

                            holder.getDayImageView().setImageResource(R.drawable.ic_smallellipseoff);
                            holder.getTextView().setTextColor(Color.parseColor("#ffffff"));
                        }
                        else {
                            holder.setClicked(true);
                            holder.getDayImageView().setImageResource(R.drawable.ic_smallellipseon);
                            holder.getTextView().setTextColor(Color.parseColor("#14223B"));
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
