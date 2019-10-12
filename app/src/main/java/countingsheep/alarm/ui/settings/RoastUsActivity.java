package countingsheep.alarm.ui.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.services.interfaces.UserService;
import countingsheep.alarm.ui.BaseActivity;

public class RoastUsActivity extends BaseActivity {
    private TextView roastusTxt;
    private EditText replyText;
    private TextView send;
    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    Typeface font;

    @Inject
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_roastus);

        activity = this;

        Injector.getActivityComponent(this).inject(this);

        font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");

        bindViews();
    }

    private void bindViews() {
        roastusTxt = findViewById(R.id.roastusTxtId);
        replyText = findViewById(R.id.replyText);
        send = findViewById(R.id.send);

        roastusTxt.setText("Give us your best shot!");
        roastusTxt.setTypeface(font);
        replyText.setTypeface(font);

        send.setOnClickListener(v -> {
            String content = replyText.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Write something if you want to send us a message", Toast.LENGTH_SHORT).show();
            } else {

                userService.sendGeneralFeedback(content, new OnResult() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(activity, "Thank you for your roastus Txt!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.roastus);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
