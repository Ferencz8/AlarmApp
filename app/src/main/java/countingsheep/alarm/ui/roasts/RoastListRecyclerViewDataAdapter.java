package countingsheep.alarm.ui.roasts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.facebook.share.Share;

import java.util.Date;
import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.ShareHelper;

import static countingsheep.alarm.util.TimeHelper.getDateStringFromDateObject;
import static countingsheep.alarm.util.TimeHelper.getHourStringFromDateObject;

public class RoastListRecyclerViewDataAdapter extends RecyclerView.Adapter<RoastListRecyclerViewHolder> {

    private List<Message> viewItemList;
    private Activity activity;
    private MessageService messageService;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private ShareHelper shareHelper;

    public RoastListRecyclerViewDataAdapter(Activity activity, List<Message> viewItemList, MessageService messageService, SharedPreferencesContainer sharedPreferencesContainer,
                                            ShareHelper shareHelper) {
        this.viewItemList = viewItemList;
        this.activity = activity;
        this.messageService= messageService;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.shareHelper = shareHelper;
    }

    @Override
    public RoastListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.content_roast_list_item_v2, parent, false);

        // Create and return our customRecycler View Holder object.
        RoastListRecyclerViewHolder ret = new RoastListRecyclerViewHolder(itemView);
        return ret;
    }


    @Override
    public void onBindViewHolder(final RoastListRecyclerViewHolder holder, int position) {
        if (viewItemList != null) {
            final Message viewItem = viewItemList.get(position);

            if (viewItem != null) {

                holder.getContentView().setText(viewItem.getContent());

                Date seenAt = viewItem.getSeenAt();
                if (seenAt != null) {
                    holder.getRoastDate().setText(getDateStringFromDateObject(seenAt));

                    holder.getRoastHour().setText(getHourStringFromDateObject(seenAt));
                }

                holder.getShareImageView().setOnClickListener(v -> {
                    shareHelper.displayShare("Hello", "This is the Santa Clause of Roasting App", new OnResult() {
                        @Override
                        public void onSuccess(Object result) {

                            messageService.markMessageShared(v.getId());
                            sharedPreferencesContainer.increaseFreeCredits(5);
                            Toast.makeText(activity, "+ 5 Credits", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(){
                            Toast.makeText(activity, "Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                holder.getPlayVoiceoverImageView().setOnClickListener(v -> {
                });

                holder.getDownloadImageView().setOnClickListener(v -> {
                });
            }
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

    public void remove(int position) {
        Message item = getRoast(position);
        if (viewItemList.contains(item)) {
            viewItemList.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    public Message getRoast(int position) {
        return viewItemList.get(position);
    }

    public void updateData(List<Message> messages) {
        this.viewItemList.clear();
        this.viewItemList.addAll(messages);
        this.notifyDataSetChanged();
    }
}