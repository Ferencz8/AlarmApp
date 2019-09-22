package countingsheep.alarm.ui.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;

public class PermissionsFragment extends Fragment {
    protected FirebaseAnalytics firebaseAnalytics;
    private static final String TAG = "PermissionsFragment";
    private TextView overlay;
    private TextView reboot;
    private Activity activity;
    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    private static PermissionsFragment fragment;
    public static synchronized PermissionsFragment newInstance() {
        if(fragment==null) {
            fragment = new PermissionsFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getActivityComponent(getActivity()).inject(PermissionsFragment.this);

        activity = getActivity();
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        firebaseAnalytics.logEvent("permissions",null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permissions, container, false);


        overlay = (TextView) view.findViewById(R.id.overlayPermissionId);
        overlay.setVisibility(View.VISIBLE);
        reboot = (TextView) view.findViewById(R.id.rebootPermissionId);
        if(doesManufacturerNeedReboot()) {
            reboot.setVisibility(View.VISIBLE);
            reboot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestBootPermission();
                }
            });
        }

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent2.setData(uri);
                startActivity(intent2);
            }
        });

        headerBar = view.findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);

        Typeface bold_font = Typeface.createFromAsset(activity.getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.permissions);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (view.getId()){
//                    case R.id.backBtn:
                        getActivity().getSupportFragmentManager().popBackStack();
//                        break;
//                }
            }
        });



        return view;
    }
    private void requestBootPermission(){
        if ("xiaomi".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {


            //this will open auto start screen where user can enable permission for your app
            Intent intentAutoStart = new Intent();
            intentAutoStart.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intentAutoStart);

        }
        if("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER) ) {

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"));
            startActivity(intent);
        }
    }

    private boolean doesManufacturerNeedReboot(){
        ArrayList<String> rebootManufacturers = new ArrayList<String>();
        rebootManufacturers.add("xiaomi");
        rebootManufacturers.add("huawei");
        return rebootManufacturers.contains(android.os.Build.MANUFACTURER.toLowerCase());
    }
}
