package countingsheep.alarm;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.settings.SettingsFragment;
import countingsheep.alarm.ui.alarmList.AlarmsFragment;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;

public class MainActivity extends BaseActivity {
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
                                hideHeaderBar(false);
                                break;
//                            case R.id.action_item2:
//                                headerBar.setVisibility(View.VISIBLE);
//                                selectedFragment = alarmsFragment;
//                                break;
                            case R.id.action_item3:
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
        titleTextView.setText(R.string.dark_sheep);
        backBtn.setVisibility(View.INVISIBLE);
        backBtn.setOnClickListener(null);
    }

    /*
    Used in order to make the app trigger on the BOOT_RECEIVED event
     */
    private void askBootPermission() {
        String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER) && !sharedPreferencesContainer.getBootReceivedOnSpecialDevices()) {

            this.dialogInteractor.displayReactiveDialog("Set Alarms Permission",
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

    private void showRemovePopup() {
        if (!this.sharedPreferencesContainer.getPopopShowedRemoveAlarm()) {
            this.sharedPreferencesContainer.setPopopShowedRemoveAlarm();

            this.dialogInteractor.displayInfoDialog(R.drawable.remove_icon, "To delete alarm swipe");
//            Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.remove_alarm_popup);
//            TextView yes = dialog.findViewById(R.id.yes_text);
//            yes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.PaymentRequestCode) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void hideHeaderBar(boolean hide){
        if(headerBar != null) {
            if(hide) {
                headerBar.setVisibility(View.GONE);
            } else {
                headerBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
