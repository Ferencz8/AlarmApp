package countingsheep.alarm;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchHandler;
import countingsheep.alarm.ui.alarmLaunch.RecreateAlarmsAtBootReceiver;
import countingsheep.alarm.ui.alarmList.AlarmsFragment;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.TimeHelper;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout headerBar;
    TextView titleTextView;
    ImageView backBtn;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bindViews();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = AlarmsFragment.newInstance();
                                titleTextView.setText(R.string.counting_sheep);
                                backBtn.setVisibility(View.GONE);
                                break;
                            case R.id.action_item2:
                                selectedFragment = AlarmsFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = AlarmsFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, AlarmsFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        Injector.getActivityComponent(this).inject(this);

        askBootPermission();
    }

    private void bindViews() {
        headerBar = findViewById(R.id.headerBar);
        titleTextView = headerBar.findViewById(R.id.titleTv);
        backBtn = headerBar.findViewById(R.id.backBtn);

        Typeface bold_font = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTextView.setTypeface(bold_font);
    }

    /*
    Used in order to make the app trigger on the BOOT_RECEIVED event
     */
    private void askBootPermission() {
        String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER) && !sharedPreferencesContainer.getBootReceivedOnSpecialDevices()) {

            this.dialogInteractor.displayDialog("Set Alarms Permission",
                    "On some devices the alarms need to be restored if the phone is shut down/restarted. " +
                            "Grant the app the permission to auto-restart", new DialogInteractor.OnReaction() {
                        @Override
                        public void onPositive() {
                            sharedPreferencesContainer.setBootReceivedOnSpecialDevices();

                            //this will open auto start screen where user can enable permission for your app
                            Intent intentAutoStart = new Intent();
                            intentAutoStart.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            startActivity(intentAutoStart);
                        }

                        @Override
                        public void onNegative() {

                        }
                    });
        }
    }
}
