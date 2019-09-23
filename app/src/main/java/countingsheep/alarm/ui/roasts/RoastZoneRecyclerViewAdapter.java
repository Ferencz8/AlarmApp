package countingsheep.alarm.ui.roasts;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.db.entities.RoastZoneItem;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class RoastZoneRecyclerViewAdapter extends RecyclerView.Adapter<RoastZoneRecyclerViewAdapter.RoastZoneRecyclerViewHolder> {
    private Activity activity;
    private List<RoastZoneItem> viewItemList;
    private RoastZoneListener listener;

    public RoastZoneRecyclerViewAdapter(Activity activity, RoastZoneListener listener) {
        this.activity = activity;
        this.viewItemList = getRoastZoneMenuItems();
        this.listener = listener;
    }

    @Override
    public RoastZoneRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_roast_zone_item, parent, false);
        RoastZoneRecyclerViewHolder vh = new RoastZoneRecyclerViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RoastZoneRecyclerViewHolder holder, int position) {
        if (viewItemList != null) {
            final RoastZoneItem item = viewItemList.get(position);

            if (item != null) {
                holder.title.setText(item.getTitle());

                holder.description.setText(item.getMessage());

                holder.isLocked.setVisibility(item.isLocked() ? VISIBLE : GONE);

                holder.itemView.setOnClickListener( v -> {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return viewItemList.size();
    }

    public class RoastZoneRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ProgressBar isLocked;

        RoastZoneRecyclerViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            isLocked = itemView.findViewById(R.id.isLocked);
        }
    }

    interface RoastZoneListener {
        void onItemClick(int position);
    }

    private List<RoastZoneItem> getRoastZoneMenuItems() {
        List<RoastZoneItem> menuItems = new ArrayList<>();

        Resources res = activity.getResources();
        String dummyText = res.getString(R.string.lorem_ipsum_small);

        RoastZoneItem item1 = new RoastZoneItem(res.getString(R.string.your_roasts_title), "Remember all those jokes about you from mornings? Thank god they are all here.", false);
        RoastZoneItem item2 = new RoastZoneItem(res.getString(R.string.roast_friend_title), "Not sure if it’s legal yet. But once we find a way, you’ll be able to piss of your friends anonymously, so that doesn’t make you a bad friend.", true);
        RoastZoneItem item3 = new RoastZoneItem(res.getString(R.string.leaderboard_title), "Here is your turn to roast DarkSheep! Do you feel funny enough to make me cry? If you succeed, your masterpiece will appear weekly in this section. Make me proud as you never did to your parents.", true);
        RoastZoneItem item4 = new RoastZoneItem(res.getString(R.string.roast_chat_title), "Wonder where you can find confused people similar to you? Roast chat will be the right place for you, roasting with others whenever & whatever bothers you.", true);
        RoastZoneItem item5 = new RoastZoneItem(res.getString(R.string.battle_field_title), "Through this, you’ll be able to finally win something. Compete with friends on morning productivity and win what others are losing. Only money, dignity is irreversible in your case.", true);

        menuItems.add(item1); menuItems.add(item2); menuItems.add(item3); menuItems.add(item4); menuItems.add(item5);

        return menuItems;
    }
}