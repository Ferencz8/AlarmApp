package countingsheep.alarm.ui.freecredits;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.db.SharedPreferencesContainer;

public class FreeCreditsActivity extends AppCompatActivity {

    ImageView gettingStarted;
    TextView freeCreditsTxt;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freecredits);

        Injector.getActivityComponent(this).inject(this);



        freeCreditsTxt = findViewById(R.id.freecreditTxt);
        gettingStarted = findViewById(R.id.gettingStartedBtn);

        gettingStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFreeCredits();

                Intent intent = new Intent(FreeCreditsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setFreeCredits() {
        if(this.sharedPreferencesContainer!=null){
            this.sharedPreferencesContainer.setFreeCredits(5);
        }
    }
}
