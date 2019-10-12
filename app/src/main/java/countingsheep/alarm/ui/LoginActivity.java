package countingsheep.alarm.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Inject;
import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;
import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.ui.settings.OnBoardingActivity;
import countingsheep.alarm.ui.settings.PrivacyPolicyActivity;
import countingsheep.alarm.ui.settings.TermsAndConditionsActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    ImageView SignUpButton;
    CheckBox tccheckBox;
    CheckBox privacyPolicyCheckBox;
    TextView termsText;
    TextView privacyText;
    Activity activity;
    ProgressBar spinner;
    private ImageView logo;
    private boolean termsAndCondIsChecked = false;
    private boolean privacyPolicyIsChecked = false;

    @Inject
    SocialAuthenticationService socialAuthenticationService;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    private void bindViews() {
        SignUpButton = findViewById(R.id.SignUpID);
        tccheckBox = findViewById(R.id.checkBox);
        privacyPolicyCheckBox = findViewById(R.id.checkBoxPrivacyPolicy);
        termsText = findViewById(R.id.Terms2);
        privacyText = findViewById(R.id.privacyPolicy_Text);
        spinner = findViewById(R.id.loadingCircle);
        logo = findViewById(R.id.component);
        spinner.setVisibility(View.INVISIBLE);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        //termsText.setTypeface(custom_font);

        termsText.setText(fromHtmlN("<p>I have read and agree to the <br><b><font color='#FFB800'>Terms of Service</font></b></p>"));
        privacyText.setText(fromHtmlN("<p>I have read and agree to the <br><b><font color='#FFB800'>Privacy Policy</font></b></p>"));
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                logo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(1000);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    private static Spanned fromHtmlN(String data) {
        Spanned result;
        result = Html.fromHtml(data);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        activity = this;

        setContentView(R.layout.activity_login);
        printKeyHash();
        bindViews();
        final Drawable drawable = getDrawable(R.drawable.ic_box_checked_true);
        final Drawable drawableOff = getDrawable(R.drawable.ic_checked_box);

        privacyPolicyCheckBox.setBackground(drawableOff);
        tccheckBox.setBackground(drawableOff);
        tccheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    termsAndCondIsChecked = true;
                    tccheckBox.setBackground(drawable);
                }
                else
                {
                    termsAndCondIsChecked = false;
                    tccheckBox.setBackground(drawableOff);
                }

                if(privacyPolicyIsChecked && isChecked){
                    SignUpButton.setAlpha(1f);
                }
                else
                {
                    SignUpButton.setAlpha(0.7f);
                }
            }
        });

        privacyPolicyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    privacyPolicyIsChecked = true;
                    privacyPolicyCheckBox.setBackground(drawable);

                } else {
                    privacyPolicyIsChecked = false;
                    privacyPolicyCheckBox.setBackground(drawableOff);
                }

                if(termsAndCondIsChecked && isChecked){
                    SignUpButton.setAlpha(1f);
                }
                else
                {
                    SignUpButton.setAlpha(0.7f);
                }
            }
        });

        SignUpButton.setOnClickListener(this);
        termsText.setOnClickListener(this);
        privacyText.setOnClickListener(this);

        if (socialAuthenticationService.isUserLoggedIn()) {
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        socialAuthenticationService.onActivityResult(requestCode, resultCode, data);
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("countingsheep.alarm", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                Log.d("KeyHash", "in");
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash", "error1");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash", "error2");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.SignUpID:
                if (!tccheckBox.isChecked() || !privacyPolicyCheckBox.isChecked()) {
                    Toast.makeText(LoginActivity.this, "Accept the terms and conditions & privacy policy! ", Toast.LENGTH_LONG).show();
                } else {

                    spinner.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    authenticationService.socialLogin(new OnSocialLoginResult() {

                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(activity, "Start OnBoarding", Toast.LENGTH_SHORT);

                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(user.id));
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user.email);
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                            if(sharedPreferencesContainer.getCurrentUserId()!=0){
//                            if(sharedPreferencesContainer.getCustomerId()!=null && sharedPreferencesContainer.getCustomerId()!=""){
                                finish();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                sharedPreferencesContainer.setCurrentUserId(user.id);
                                finish();
                                Intent intent = new Intent(LoginActivity.this, OnBoardingActivity.class);
                                startActivity(intent);
                            }


                            spinner.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                        @Override
                        public void onCancel() {
                            spinner.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                        @Override
                        public void onError(Exception exception) {
                            Toast.makeText(LoginActivity.this, "Try again!", Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                }
                break;
            case R.id.Terms2:
                Intent intent = new Intent(LoginActivity.this, TermsAndConditionsActivity.class);
                startActivity(intent);
                break;
            case R.id.privacyPolicy_Text:
                Intent intent2 = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent2);
                break;
        }
    }
}