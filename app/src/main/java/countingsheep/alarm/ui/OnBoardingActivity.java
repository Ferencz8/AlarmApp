package countingsheep.alarm.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.ui.adapters.SliderAdapter;

public class OnBoardingActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private LinearLayout sliderDots;
    private TextView[] dots;
    private Button slideBackBtn;
    private Button slideNextBtn;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        bindViews();

        slideBackBtn.setVisibility(View.GONE);
        pagerAdapter = new SliderAdapter(OnBoardingActivity.this);
        viewPager.setAdapter(pagerAdapter);
        addDotsIndicator(0);

        slideBackBtn.setOnClickListener(this);
        slideNextBtn.setOnClickListener(this);
        viewPager.addOnPageChangeListener(listener);

    }

    private void bindViews(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        slideBackBtn = (Button) findViewById(R.id.slideBackBtn);
        slideNextBtn = (Button) findViewById(R.id.slideNextBtn);
    }

    public void addDotsIndicator(int position){
        dots = new TextView[3];
        sliderDots.removeAllViews();
        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            sliderDots.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage = i;
            if(viewPager.getCurrentItem() != 0){
                slideBackBtn.setVisibility(View.VISIBLE);
            } else {
                slideBackBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.slideBackBtn:
                viewPager.setCurrentItem(currentPage - 1);
                break;
            case R.id.slideNextBtn:
                if(viewPager.getCurrentItem() < pagerAdapter.getCount()- 1){
                    viewPager.setCurrentItem(currentPage+1);
                } else if(viewPager.getCurrentItem() == pagerAdapter.getCount() - 1){
                    Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }
}
