package countingsheep.alarm.ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.db.SharedPreferencesContainer;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;
    private EditText phoneNumberTxtView;
    private TextView saveBtn;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Injector.getActivityComponent(this).inject(this);

        bindViews();
    }

    private void bindViews(){
        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.profile);
        backBtn.setOnClickListener(this);

        phoneNumberTxtView = findViewById(R.id.phone_number_text);
        String phoneNumber = this.sharedPreferencesContainer.getCurrentUserPhoneNumber();
        if(!TextUtils.isEmpty(phoneNumber)) {
            phoneNumberTxtView.setText(phoneNumber);
        }

        saveBtn = findViewById(R.id.save_profile_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesContainer.setCurrentUserPhoneNumber(phoneNumberTxtView.getText().toString());
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
