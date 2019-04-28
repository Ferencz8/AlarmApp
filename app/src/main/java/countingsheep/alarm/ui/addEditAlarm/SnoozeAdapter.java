package countingsheep.alarm.ui.addEditAlarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import countingsheep.alarm.R;

public class SnoozeAdapter extends RecyclerView.Adapter<SnoozeAdapter.ItemViewHolder> {

    public int selectedItem = -1;
    private ArrayList<Integer> durations;


    public SnoozeAdapter( ArrayList<Integer> durations){
        this.durations = durations;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.snooze_recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.radioButton.setChecked(position == selectedItem);
        if(durations.get(position) == 1){
            holder.durationTextView.setText(String.valueOf(durations.get(position)) + " minute");
        } else {
            holder.durationTextView.setText(String.valueOf(durations.get(position)) + " minutes");
        }
    }

    @Override
    public int getItemCount() {
        return durations.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView durationTextView;

       public ItemViewHolder(@NonNull View itemView) {
           super(itemView);
           radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
           durationTextView = (TextView) itemView.findViewById(R.id.durationTv);

           View.OnClickListener clickListener = new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                       selectedItem = getAdapterPosition();
                       notifyItemChanged(0, durations.size());
                       notifyDataSetChanged();
               }
           };

           itemView.setOnClickListener(clickListener);
           radioButton.setOnClickListener(clickListener);
       }
   }
}
