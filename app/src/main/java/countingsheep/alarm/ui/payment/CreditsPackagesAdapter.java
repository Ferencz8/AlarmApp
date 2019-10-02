package countingsheep.alarm.ui.payment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.db.entities.CreditsPackage;
import countingsheep.alarm.util.CustomTypefaceSpan;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class CreditsPackagesAdapter extends RecyclerView.Adapter<CreditsPackagesAdapter.CreditsPackageViewHolder> {
    private Context ctx;
    private List<CreditsPackage> items;
    private CreditsPackageListener listener;
    private int selectedPosition;

    private Typeface regularFont;
    private Typeface boldFont;
    private Typeface italicFont;

    public CreditsPackagesAdapter(Context ctx, CreditsPackageListener listener) {
        this.ctx = ctx;
        this.items = getItems();
        this.listener = listener;

        selectedPosition = -1;

        regularFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        boldFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        italicFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/AvenirNextLTPro-It.otf");
    }

    @NonNull
    @Override
    public CreditsPackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credits_package, parent, false);
        CreditsPackageViewHolder vh = new CreditsPackageViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CreditsPackageViewHolder holder, int position) {
        if (items != null) {
            final CreditsPackage item = items.get(position);

            if (item != null) {
                holder.title.setTypeface(regularFont);
                holder.title.setText(item.getTitle());

                String strCredits = item.isEndless() ? ctx.getString(R.string.endless) : item.getCredits() + "";
                String strSnoozesNumber = ctx.getString(R.string.snoozes_number_template, strCredits);
                SpannableStringBuilder creditsSSB = new SpannableStringBuilder(strSnoozesNumber);
                creditsSSB.setSpan (new CustomTypefaceSpan("", boldFont), 0, strCredits.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                creditsSSB.setSpan (new CustomTypefaceSpan("", regularFont), strCredits.length() + 1, strSnoozesNumber.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.credits.setText(creditsSSB);

                holder.description.setTypeface(regularFont);
                holder.description.setText(item.getDescription());

                holder.price.setTypeface(boldFont);
                holder.price.setText(ctx.getString(R.string.price_template, item.getCost()));

                holder.priceHint.setTypeface(italicFont);

                holder.selector.setVisibility(selectedPosition == position ? VISIBLE : GONE);

                holder.itemView.setOnClickListener( v -> {
                    if (listener != null) {
                        listener.onItemClick(item);

                        selectedPosition = position;
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CreditsPackageViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView credits;
        TextView description;
        TextView price;
        TextView priceHint;
        Group selector;

        CreditsPackageViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.packageTitle);
            credits = itemView.findViewById(R.id.packageCredits);
            description = itemView.findViewById(R.id.packageDescription);
            price = itemView.findViewById(R.id.packagePrice);
            priceHint = itemView.findViewById(R.id.packagePriceHint);
            selector = itemView.findViewById(R.id.selector);
        }
    }

    private List<CreditsPackage> getItems() {
        List<CreditsPackage> menuItems = new ArrayList<>();

        String dummyTitle = ctx.getString(R.string.dummy_credits_package_title);
        String dummyDescription = ctx.getString(R.string.dummy_credits_package_description);

        CreditsPackage item1 = new CreditsPackage("Hookup", 5, false, "Poor \n Estimated 1 week relationship.", 5);
        CreditsPackage item2 = new CreditsPackage("Friend Zone", 25, false, "Good start for a creeper, 1 month relationship.", 20);
        CreditsPackage item3 = new CreditsPackage("Lucky Strike", 50, false, "Damn, you really snooze! Pull my finger.", 40);
        CreditsPackage item4 = new CreditsPackage("Marriage", 1000, true, "All you can or cannot eat, roasted for life!", 100);

        menuItems.add(item1); menuItems.add(item2); menuItems.add(item3); menuItems.add(item4);

        return menuItems;
    }


    interface CreditsPackageListener {
        void onItemClick(CreditsPackage item);
    }


    public static class CreditsPackageItemDecorator extends RecyclerView.ItemDecoration {
        private int space;

        CreditsPackageItemDecorator(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}