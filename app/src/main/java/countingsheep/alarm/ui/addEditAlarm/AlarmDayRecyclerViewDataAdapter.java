package countingsheep.alarm.ui.addEditAlarm;

import android.app.Activity;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import countingsheep.alarm.R;

public class AlarmDayRecyclerViewDataAdapter extends RecyclerView.Adapter<AlarmDayRecyclerViewHolder> {

    private static int DayNotSelectedImage = R.drawable.ic_smallellipseoff;
    private static int DaySelectedImage = R.drawable.ic_smallellipseon;

    private List<AlarmDayRecyclerViewItem> viewItemList;

    private List<String> clickedItemsList;

    private Activity activity;

    public List<String> getClickedItemsList() {
        return clickedItemsList;
    }

    public AlarmDayRecyclerViewDataAdapter(Activity activity, List<AlarmDayRecyclerViewItem> viewItemList) {
        this.set(viewItemList);
        this.activity = activity;
        clickedItemsList = new ArrayList<>();
    }

    public void set(List<AlarmDayRecyclerViewItem> viewItemList) {
        this.viewItemList = viewItemList;
        notifyDataSetChanged();
    }

    public void markDaysAsSelected(List<String> itemsText) {
        for (AlarmDayRecyclerViewItem item : this.viewItemList) {
            if(itemsText.contains(item.getText())) {
                item.setImageResourceId(DaySelectedImage);
            }
        }

        notifyDataSetChanged();
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
        if (viewItemList != null) {
            AlarmDayRecyclerViewItem viewItem = viewItemList.get(position);

            if (viewItem != null) {
                holder.getTextView().setText(viewItem.getText());
                holder.getDayImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()) {

                            markUnSelected(holder);

                            clickedItemsList.remove(holder.getTextView().getText().toString());
                        } else {
                            markSelected(holder);

                            clickedItemsList.add(holder.getTextView().getText().toString());
                        }
                    }
                });


                //for edit repeat days
                if(viewItem.getImageResourceId() > 0){
                    markSelected(holder);
                    clickedItemsList.add(viewItem.getText());
                }
            }
        }
    }

    public void markUnSelected(AlarmDayRecyclerViewHolder holder){
        holder.setClicked(false);

        holder.getDayImageView().setImageResource(DayNotSelectedImage);
        holder.getTextView().setTextColor(activity.getResources().getColor(R.color.white_text_color, null));
    }

    private void markSelected(AlarmDayRecyclerViewHolder holder){
        holder.setClicked(true);
        holder.getDayImageView().setImageResource(DaySelectedImage);
        holder.getTextView().setTextColor(activity.getResources().getColor(R.color.bt_color_primary_dark, null));
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (viewItemList != null) {
            ret = viewItemList.size();
        }
        return ret;
    }
}
