package countingsheep.alarm.ui.alarmLaunch;

import android.graphics.Typeface;
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
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.services.interfaces.UserService;
import countingsheep.alarm.ui.BaseActivity;

public class AlarmRoastActivity extends BaseActivity {
    private Group roastLayout;
    private TextView roast;
    private ImageView share;
    private TextView reply;
    private Group replyLayout;
    private EditText replyText;
    private TextView send;

    public static final String ARG_EXTRA_MSG_ID = "arg_extra_msg_id";
    public static final String ARG_EXTRA_ROAST = "arg_extra_roast";
    public static final String ARG_IS_FEEDBACK = "arg_is_feedback";
    private int msgId;
    private String roastMsg;
    private boolean isFeedback;
    
    Typeface font;

    @Inject
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_roast);

        Injector.getActivityComponent(this).inject(this);

        if (getIntent() != null) {
            msgId = getIntent().getIntExtra(ARG_EXTRA_MSG_ID, -1);
            roastMsg = getIntent().getStringExtra(ARG_EXTRA_ROAST);
            isFeedback = getIntent().getBooleanExtra(ARG_IS_FEEDBACK, false);
        }

        font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");

        bindViews();
    }

    private void bindViews() {
        roastLayout = findViewById(R.id.roastLayout);
        roast = findViewById(R.id.roast);
        reply = findViewById(R.id.reply);
        share = findViewById(R.id.share);
        replyLayout = findViewById(R.id.replyLayout);
        replyText = findViewById(R.id.replyText);
        send = findViewById(R.id.send);

        roast.setText(roastMsg);
        roast.setTypeface(font);
        reply.setTypeface(font);
        replyText.setTypeface(font);
        
        reply.setOnClickListener(v -> replyLayout.setVisibility(View.VISIBLE));
        
        if (isFeedback) {
            roastLayout.setVisibility(View.VISIBLE);
            replyLayout.setVisibility(View.VISIBLE);
        }

        send.setOnClickListener(v -> {
            String content = replyText.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Write something if you want to send us a message", Toast.LENGTH_SHORT).show();
            } else {
                if (isFeedback) {
                    userService.sendGeneralFeedback(content, new OnResult() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(activity, "Thank you for your roast", Toast.LENGTH_SHORT).show();
                            AlarmRoastActivity.this.finish();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    userService.sendRoastFeedback(msgId, content, new OnResult() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(activity, "Thank you for your comeback", Toast.LENGTH_SHORT).show();
                            AlarmRoastActivity.this.finish();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}