package countingsheep.alarm.ui.roasts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.facebook.share.Share;

import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.ShareHelper;

public class RoastListRecyclerViewDataAdapter extends RecyclerView.Adapter<RoastListRecyclerViewHolder> {

    private List<Message> viewItemList;
    private Activity activity;
    private MessageService messageService;
    private SharedPreferencesContainer sharedPreferencesContainer;

    public RoastListRecyclerViewDataAdapter(Activity activity, List<Message> viewItemList, MessageService messageService, SharedPreferencesContainer sharedPreferencesContainer) {
        this.viewItemList = viewItemList;
        this.activity = activity;
        this.messageService= messageService;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
    }

    @Override
    public RoastListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.content_roast_list_item, parent, false);

        // Create and return our customRecycler View Holder object.
        RoastListRecyclerViewHolder ret = new RoastListRecyclerViewHolder(itemView);
        return ret;
    }


    @Override
    public void onBindViewHolder(final RoastListRecyclerViewHolder holder, int position) {
        if (viewItemList != null) {
            // Get car item dto in list.
            final Message viewItem = viewItemList.get(position);

            if (viewItem != null) {

                holder.getContentView().setText(viewItem.getContent());
                holder.getRoastLayout().setBackgroundColor(activity.getResources().getColor(R.color.colorSecondary, null));

                View.OnClickListener shareClickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sharedPreferencesContainer.setUserInitiatedShare(true);
                        ShareHelper shareHelper = new ShareHelper(activity);
                        shareHelper.displayShare("Hello", "This is the Santa Clause of Roasting App");
                        messageService.markMessageShared(v.getId());
                    }
                };

                holder.getShareImageView().setOnClickListener(shareClickListener);

                holder.getPlayVoiceoverImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.isClicked()) {
                            holder.setClicked(false);

                            holder.getPlayVoiceoverImageView().setImageResource(R.drawable.ic_sheepoff);
                            //TODO:: Pause THE SOUND
                        }
                        else {
                            holder.setClicked(true);
                            holder.getPlayVoiceoverImageView().setImageResource(R.drawable.ic_sheepon);
                            //TODO:: Play THE SOUND
                        }
                    }
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