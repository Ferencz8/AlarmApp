package countingsheep.alarm.ui.settings;

import android.app.Activity;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.adapters.SliderAdapter;
import countingsheep.alarm.ui.freecredits.FreeCreditsActivity;
import countingsheep.alarm.ui.payment.BraintreePaymentInteractor;
import countingsheep.alarm.ui.payment.GetCreditsActivity;
import countingsheep.alarm.ui.payment.OnPaymentInteractionResult;

public class OnBoardingActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private SliderAdapter pagerAdapter;
    private LinearLayout sliderDots;
    private ImageView[] dots;
    private Button slideBackBtn;
    private Button slideNextBtn;
    private ProgressBar loadingSpinner;
    private int currentPage;
    private boolean reachedPaymentSlide = false;

    @Inject
    BraintreePaymentInteractor braintreePaymentInteractor;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        bindViews();

        Injector.getActivityComponent(this).inject(this);

        slideBackBtn.setVisibility(View.GONE);
        pagerAdapter = new SliderAdapter(OnBoardingActivity.this, this.sharedPreferencesContainer.getDisplayPaymentInOnBoarding());
        viewPager.setAdapter(pagerAdapter);
        addDotsIndicator(0);

        slideBackBtn.setOnClickListener(this);
        slideNextBtn.setOnClickListener(this);
        viewPager.addOnPageChangeListener(listener);

    }

    private void bindViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        slideBackBtn = (Button) findViewById(R.id.slideBackBtn);
        slideNextBtn = (Button) findViewById(R.id.slideNextBtn);
        loadingSpinner = (ProgressBar) findViewById(R.id.onBoardingProgressBar);
        loadingSpinner.setVisibility(View.INVISIBLE);
    }

    public void addDotsIndicator(int position) {
        dots = new ImageView[pagerAdapter.getCount()];
        sliderDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getDrawable(R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            sliderDots.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[position].setImageDrawable(getDrawable(R.drawable.active_dot));
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
            if (viewPager.getCurrentItem() != 0) {
                slideBackBtn.setVisibility(View.VISIBLE);

                if (viewPager.getCurrentItem() == pagerAdapter.getCount() - 1 && sharedPreferencesContainer.getDisplayPaymentInOnBoarding()) {
                    slideBackBtn.setText("Free trial");
                    slideNextBtn.setText("Get Credits");
                    reachedPaymentSlide = true;
                } else {
                    slideBackBtn.setText("Back");
                    slideNextBtn.setText("Next");
                }
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
        switch (view.getId()) {
            case R.id.slideBackBtn:
                if (reachedPaymentSlide && this.sharedPreferencesContainer.shouldGiveFreeCredits()) {
                    firebaseAnalytics.logEvent("onboarding_payment_not_now", null);
                    finish();
                    Intent intent = new Intent(OnBoardingActivity.this, FreeCreditsActivity.class);
                    startActivity(intent);
                }
                viewPager.setCurrentItem(currentPage - 1);
                break;
            case R.id.slideNextBtn:
                if (reachedPaymentSlide) {
                    firebaseAnalytics.logEvent("onboarding_payment_now", null);
                    //displayPaymentDropIn();

                    finish();
                    startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
                    startActivity(new Intent(OnBoardingActivity.this, GetCreditsActivity.class));
                }

                if (viewPager.getCurrentItem() < pagerAdapter.getCount() - 1) {
                    viewPager.setCurrentItem(currentPage + 1);
                }
//                else if(viewPager.getCurrentItem() == pagerAdapter.getCount() - 1){
//                    Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }

                break;
        }
    }

    private void displayPaymentDropIn() {

        loadingSpinner.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.sharedPreferencesContainer.setDisplayPaymentInOnBoarding(false);

        braintreePaymentInteractor.displayPaymentMethods(new OnPaymentInteractionResult() {
            @Override
            public void onSuccess() {
                loadingSpinner.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                finish();
                Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCanceled() {
                loadingSpinner.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (sharedPreferencesContainer.shouldGiveFreeCredits()) {
                    finish();
                    Intent intent = new Intent(OnBoardingActivity.this, FreeCreditsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        braintreePaymentInteractor.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED && this.sharedPreferencesContainer.shouldGiveFreeCredits()) {
            loadingSpinner.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            firebaseAnalytics.logEvent("onboarding_payment_now_canceled", null);

            finish();
            Intent intent = new Intent(OnBoardingActivity.this, FreeCreditsActivity.class);
            startActivity(intent);
        }
    }
}
