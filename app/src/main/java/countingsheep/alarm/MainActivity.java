package countingsheep.alarm;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.ui.SettingsFragment;
import countingsheep.alarm.ui.alarmList.AlarmsFragment;
import countingsheep.alarm.ui.shared.DialogInteractor;

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
                                headerBar.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_item2:
                                headerBar.setVisibility(View.VISIBLE);
                                selectedFragment = AlarmsFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                headerBar.setVisibility(View.GONE);
                                selectedFragment = SettingsFragment.newInstance();
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

        showRemovePopup();
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

    private void showRemovePopup(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.remove_alarm_popup);
        TextView yes = dialog.findViewById(R.id.yes_text);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
