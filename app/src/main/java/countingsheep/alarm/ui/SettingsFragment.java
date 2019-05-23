package countingsheep.alarm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import countingsheep.alarm.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private CircleImageView userPhoto;
    private TextView username;
    private TextView termsAndConditions;
    private TextView feedback;
    private TextView onBoarding;
    private TextView alarmHistory;
    private TextView logout;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // Injector.getActivityComponent(getActivity()).inject(SettingsFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        bindViews(view);

        return view;
    }

    private void bindViews(View view){
        userPhoto = view.findViewById(R.id.user_photo);
        username = view.findViewById(R.id.username);
        termsAndConditions = view.findViewById(R.id.terms_text);
        termsAndConditions.setOnClickListener(this);
        feedback = view.findViewById(R.id.feedback_text);
        feedback.setOnClickListener(this);
        onBoarding = view.findViewById(R.id.onBoarding_text);
        onBoarding.setOnClickListener(this);
        alarmHistory = view.findViewById(R.id.history_text);
        alarmHistory.setOnClickListener(this);
        logout = view.findViewById(R.id.logout_text);
        logout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.terms_text:
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_text:
                break;
            case R.id.onBoarding_text:
                Intent intent1 = new Intent(getActivity(), OnBoardingActivity.class);
                startActivity(intent1);
                break;
            case R.id.history_text:
                break;
            case R.id.logout_text:
                showLogoutPopup();
                break;
        }
    }

    private void showLogoutPopup(){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.logout_popup);
        TextView yes = dialog.findViewById(R.id.yes_textview);
        TextView cancel = dialog.findViewById(R.id.cancel_text);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO: Log out
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
