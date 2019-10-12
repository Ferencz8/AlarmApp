package countingsheep.alarm.ui.alarmLaunch;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.services.interfaces.UserService;
import countingsheep.alarm.infrastructure.ShareHelper;
import countingsheep.alarm.ui.BaseActivity;

public class AlarmRoastActivity extends BaseActivity {
    private Group roastLayout;
    private TextView roast;
    private Group replyLayout;
    private EditText replyText;
    private TextView replyBtn;
    private TextView send;
    private ImageView share;

    public static final String ARG_EXTRA_MSG_ID = "arg_extra_msg_id";
    public static final String ARG_EXTRA_ROAST = "arg_extra_roast";
    private int msgId;
    private String roastMsg;

    Typeface font;

    @Inject
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_roastreply);

        activity = this;

        Injector.getActivityComponent(this).inject(this);

        if (getIntent() != null) {
            msgId = getIntent().getIntExtra(ARG_EXTRA_MSG_ID, -1);
            roastMsg = getIntent().getStringExtra(ARG_EXTRA_ROAST);
        }

        font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");

        bindViews();
    }

    private void bindViews() {
        roastLayout = findViewById(R.id.roastLayout);
        roast = findViewById(R.id.roast);
        replyLayout = findViewById(R.id.replyLayout);
        replyLayout.setVisibility(View.GONE);
        replyText = findViewById(R.id.replyText);
        replyBtn = findViewById(R.id.reply);
        send = findViewById(R.id.send);
        share = findViewById(R.id.share);
        share.setVisibility(View.GONE);
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //new ShareHelper(activity).displayShare("Subject", "Body");
//            }
//        });
        roast.setText(roastMsg);
        roast.setTypeface(font);
        replyText.setTypeface(font);

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyLayout.setVisibility(View.VISIBLE);
            }
        });
        send.setOnClickListener(v -> {
            String content = replyText.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Write something if you want to send us a message", Toast.LENGTH_SHORT).show();
            } else {
                userService.sendRoastFeedback(msgId, content, new OnResult() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(activity, "Thank you for your comeback", Toast.LENGTH_SHORT).show();
                        AlarmRoastActivity.this.finish();
                        Intent setIntent = new Intent(AlarmRoastActivity.this, MainActivity.class);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(AlarmRoastActivity.this, MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}