package countingsheep.alarm.ui;

import android.graphics.Typeface;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import countingsheep.alarm.R;

public class TermsAndConditionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView termAndConditionsTv;
    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        bindViews();
        titleTv.setText(R.string.terms_and_conditions);
        termAndConditionsTv.setText(R.string.large_text);
        backBtn.setOnClickListener(this);
    }

    private void bindViews(){
        termAndConditionsTv = findViewById(R.id.termsAndConditionsTv);
        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
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
