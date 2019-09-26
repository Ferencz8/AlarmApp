package countingsheep.alarm;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.NetworkStateReceiver;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.alarmList.AlarmsFragment;
import countingsheep.alarm.ui.roasts.RoastHistoryFragment;
import countingsheep.alarm.ui.roasts.RoastZoneFragment;
import countingsheep.alarm.ui.settings.SettingsFragment;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;

import static countingsheep.alarm.ui.payment.GetCreditsActivity.GOOGLE_PAY_REQUEST_CODE;

public class MainActivity extends BaseActivity {
    ConstraintLayout headerBar;
    TextView titleTextView;
    ImageView backBtn;
    private NetworkStateReceiver networkStateReceiver;
    private NetworkStateReceiver.NetworkStateReceiverListener networkStateReceiverListener;

    @Inject
    DialogInteractor dialogInteractor;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Inject
    SMSService smsService;

    @Inject
    MessageService messageService;

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
                            case R.id.action_item2:
                                selectedFragment = RoastZoneFragment.newInstance();
                                hideHeaderBar(false);
                                break;
                            case R.id.action_item3:
                                selectedFragment = SettingsFragment.newInstance();
                                break;
                        }
                        setFragment(selectedFragment);
                        return true;
                    }
                });
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        if(intent!= null && intent.getIntExtra("fragment", 0) == 2){

            //transaction.replace(R.id.frame_layout, RoastHistoryFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.action_item2);
            this.setFragment(RoastHistoryFragment.newInstance(), true);
        }
        else {
            transaction.replace(R.id.frame_layout, AlarmsFragment.newInstance());

        }
        transaction.commit();
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        Injector.getActivityComponent(this).inject(this);

        askBootPermission();


        networkStateReceiverListener =  messageService.getRoastMessageNetworkStateListener(this);//smsService.getSMSNetworkStateListener();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(networkStateReceiverListener);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void setFragment(Fragment fragment) {
        setFragment(fragment, false);
    }

    public void setFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();

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
        if("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER) ) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(this);
            builder.setTitle("Huawei Special").setMessage("Huawei Special")
                    .setPositiveButton("Do it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"));
                            startActivity(intent);
                            //sp.edit().putBoolean("protected",true).commit();
//                            try {
//                                String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                    cmd += " --user " + getUserSerial();
//                                }
//                                Runtime.getRuntime().exec(cmd);
//                            } catch (IOException ignored) {
//                            }
                        }
                    }).create().show();
        }
    }

//    private String getUserSerial() {
//        //noinspection ResourceType
//        Object userManager = getSystemService("user");
//        if (null == userManager) return "";
//
//        try {
//            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
//            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
//            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
//            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
//            if (userSerial != null) {
//                return String.valueOf(userSerial);
//            } else {
//                return "";
//            }
//        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
//        }
//        return "";
//    }
    private void showRemovePopup() {
        if (this.sharedPreferencesContainer.getAlarmsCountThatWereSet() >= 3 && !this.sharedPreferencesContainer.getPopopShowedRemoveAlarm()) {
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
        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (fragment != null && fragment instanceof SettingsFragment) {
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



    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(networkStateReceiverListener);
        this.unregisterReceiver(networkStateReceiver);
    }
}
