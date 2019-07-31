package countingsheep.alarm.ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;
    private EditText phoneNumber;
    private Button saveBtn;

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
