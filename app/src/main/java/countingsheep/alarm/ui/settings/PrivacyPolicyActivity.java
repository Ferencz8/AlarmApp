package countingsheep.alarm.ui.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import countingsheep.alarm.R;

public class PrivacyPolicyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView privacyPolicyTv;
    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        bindViews();
    }

    private void bindViews() {
        privacyPolicyTv = findViewById(R.id.privacyPolicyTv);
        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.privacy_policy);
        privacyPolicyTv.setText(R.string.large_text2);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
    }
}