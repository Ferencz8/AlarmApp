package countingsheep.alarm.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;

public class LoginActivity extends AppCompatActivity {

    ImageView SignUpButton;
    CheckBox checkBox;
    TextView termsText;
    TextView termsText1;
    Activity activity;

    @Inject
    SocialAuthenticationService socialAuthenticationService;
    @Inject
    AuthenticationService authenticationService;

    private void bindViews() {
        SignUpButton = findViewById(R.id.SignUpID);
        checkBox = findViewById(R.id.checkBox);
        termsText = findViewById(R.id.Terms2);
        termsText1 = findViewById(R.id.Terms1);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        termsText1.setTypeface(custom_font);
        termsText.setTypeface(bold_font);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(this).inject(this);

        setContentView(R.layout.activity_login);
        activity = this;
        //printKeyHash();
        bindViews();
        final Drawable drawable = getDrawable(R.drawable.ic_box_checked_true);
        final Drawable drawableOff = getDrawable(R.drawable.ic_checked_box);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SignUpButton.setImageResource(R.drawable.ic_group_21);
                    checkBox.setBackground(drawable);

                } else {
                    SignUpButton.setImageResource(R.drawable.ic_facebook_signup1);
                    checkBox.setBackground(drawableOff);
                }
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(LoginActivity.this, "Accept the terms and conditions! ", Toast.LENGTH_LONG).show();
                } else {
                    //TODO: remove on production
                   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                   startActivity(intent);
//                    authenticationService.socialLogin(new OnSocialLoginResult() {
//
//                        @Override
//                        public void onSuccess(User user) {
//                            Toast.makeText(activity, "Start OnBoarding", Toast.LENGTH_SHORT);
//
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//
//                        @Override
//                        public void onError(Exception exception) {
//                            Toast.makeText(LoginActivity.this, "Try again!", Toast.LENGTH_LONG).show();
//                        }
//                    });
                }
            }
        });

        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, TermsAndConditionsActivity.class);
//                startActivity(intent);
                Toast.makeText(activity, "This is a term", Toast.LENGTH_SHORT);
            }
        });
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
}