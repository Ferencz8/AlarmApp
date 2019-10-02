package countingsheep.alarm.ui.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import countingsheep.alarm.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    private int[] slide_images = {
            R.drawable.ic_onboarding1st,
            R.drawable.ic_onboarding2nd,
            R.drawable.ic_onboarding3rd
    };

    private String[] slide_texts = {
            "Basically, this is an alarm app.\nYou set the time you want to wake\nup at, and go to sleep. The good\npart comes in the morning.",
            "Cause if you “Snooze”, you lose.\nMoney (credits), and some dignity.\nAfter snoozing, we will also roast you.\nHope that’s ok.",
            "Let's get started by buying credits!\nOr whatever, if you’re poor, take the\nfree trial. Anyway, see you in the\nmorning!"
    };

    public SliderAdapter(Context context, boolean displayPayment){
        this.context = context;
        if(!displayPayment){
            slide_texts = Arrays.copyOf(slide_texts, slide_texts .length-1);
        }
    }

    @Override
    public int getCount() {
        return slide_texts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (ConstraintLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_slide_page, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideText = (TextView) view.findViewById(R.id.slideText);

        slideImage.setImageResource(slide_images[position]);
        slideText.setText(slide_texts[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);

    }
}
