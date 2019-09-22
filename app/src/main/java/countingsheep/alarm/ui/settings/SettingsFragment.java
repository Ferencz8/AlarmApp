package countingsheep.alarm.ui.settings;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import countingsheep.alarm.BuildConfig;
import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.EMailServiceImpl;
import countingsheep.alarm.network.tasks.ProfilePictureTask;
import countingsheep.alarm.network.tasks.ProfilePictureTaskResponse;
import countingsheep.alarm.ui.payment.BraintreePaymentInteractor;
import countingsheep.alarm.ui.payment.OnPaymentInteractionResult;
import countingsheep.alarm.util.Constants;
import de.hdodenhof.circleimageview.CircleImageView;
import countingsheep.alarm.payment.google_pay.GooglePaymentsUtil;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SettingsFragment";

    protected FirebaseAnalytics firebaseAnalytics;
    private CircleImageView userPhoto;
    private TextView username;
    private TextView termsAndConditions;
    private TextView feedback;
    private TextView onBoarding;
    private TextView payment;
    private TextView alarmHistory;
    private TextView logout;
    private TextView spentTextView;
    private TextView cashTextView;
    private TextView alarmCount;
    private TextView snoozeRate;
    private TextView profile;
    private TextView permissions;
    private TextView googlePayBtn;
    private ProgressBar loadingSpinner;

    private PaymentsClient paymentsClient;
    public static final int GOOGLE_PAY_REQUEST_CODE = 100;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    BraintreePaymentInteractor braintreePaymentInteractor;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;

    @Inject
    EMailServiceImpl eMailService;

    @Inject
    PaymentService paymentService;

    @Inject
    AlarmReactionService alarmReactionService;


    private static SettingsFragment instance;

    public static synchronized SettingsFragment newInstance() {
        if (instance == null) {
            instance = new SettingsFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getActivityComponent(getActivity()).inject(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        firebaseAnalytics.logEvent("settings", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        bindViews(view);
        Log.e(SettingsFragment.class.getName(), "onCreateView");
        ((MainActivity) getActivity()).hideHeaderBar(true);
        if (sharedPreferencesContainer.getFreeCredits() != 0) {
            this.spentTextView.setText(getString(R.string.creditsLeft));
            this.cashTextView.setText(sharedPreferencesContainer.getFreeCredits() + " $");
        } else {
            this.spentTextView.setText(getString(R.string.spent));
            //TODO:: add real spent money
            paymentService.getSumAmount(new OnAsyncResponse<Integer>() {
                @Override
                public void processResponse(Integer response) {
                    if (response == null) {
                        response = 0;
                    }
                    cashTextView.setText(String.valueOf(response) + " $");

                }
            });
        }

        alarmReactionService.getAlarmsCount(new OnAsyncResponse<Integer>() {
            @Override
            public void processResponse(Integer response) {
                if (response == null) {
                    response = 0;
                }
                alarmCount.setText(String.valueOf(response));
            }
        });

        alarmReactionService.getSnoozeRate(new OnAsyncResponse<Integer>() {
            @Override
            public void processResponse(Integer response) {
                if (response == null) {
                    response = 0;
                }
                snoozeRate.setText(String.valueOf(response * 100) + " %");
            }
        });

        paymentsClient = GooglePaymentsUtil.createPaymentsClient(requireActivity());
        canShowGooglePayButton();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferencesContainer.resetUsernameTokensCount();

    }

    private void bindViews(View view) {
        spentTextView = view.findViewById(R.id.spent);
        cashTextView = view.findViewById(R.id.cash_text);
        userPhoto = view.findViewById(R.id.user_photo);
        loadProfilePicture();
        permissions = view.findViewById(R.id.permissions_text);
        if (!doesManufacturerNeedSpecialPermissions()) {
            permissions.setVisibility(View.GONE);
        } else {
            permissions.setVisibility(View.VISIBLE);
            permissions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity act = (MainActivity) getActivity();
                    if (act != null) act.setFragment(PermissionsFragment.newInstance(), true);
                }
            });
        }
        username = view.findViewById(R.id.username);
        username.setText(this.sharedPreferencesContainer.getFullname());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferencesContainer.getUsernameTokensCount() >= 3) {
                    Toast.makeText(getContext(), "+10 credits", Toast.LENGTH_LONG).show();
                    sharedPreferencesContainer.increaseFreeCredits(10);
                    sharedPreferencesContainer.resetUsernameTokensCount();
                } else {
                    sharedPreferencesContainer.increaseUsernameTokensCount();
                }
            }
        });

        termsAndConditions = view.findViewById(R.id.terms_text);
        termsAndConditions.setOnClickListener(this);
        feedback = view.findViewById(R.id.feedback_text);
        feedback.setOnClickListener(this);
        onBoarding = view.findViewById(R.id.onBoarding_text);
        onBoarding.setOnClickListener(this);
        payment = view.findViewById(R.id.payment_text);
        payment.setOnClickListener(this);
        alarmHistory = view.findViewById(R.id.history_text);
        alarmHistory.setOnClickListener(this);
        logout = view.findViewById(R.id.logout_text);
        logout.setOnClickListener(this);
        alarmCount = view.findViewById(R.id.alarm_text);
        snoozeRate = view.findViewById(R.id.snooze_text);
        loadingSpinner = view.findViewById(R.id.settingsProgressBar);
        loadingSpinner.setVisibility(View.INVISIBLE);
        profile = view.findViewById(R.id.profile_text);
        profile.setOnClickListener(this);
        googlePayBtn = view.findViewById(R.id.get_credits);
    }

    private boolean doesManufacturerNeedSpecialPermissions() {
        ArrayList<String> specialManufacturers = new ArrayList<String>();
        specialManufacturers.add("xiaomi");
        specialManufacturers.add("huawei");
        return specialManufacturers.contains(android.os.Build.MANUFACTURER.toLowerCase());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new ProfilePictureTask(this.getContext(), this.sharedPreferencesContainer.getProfilePictureUrl(), new ProfilePictureTaskResponse() {
                        @Override
                        public void OnImageAvailable(@NonNull String imagePath) {
                            sharedPreferencesContainer.setProfilePictureLocalPath(imagePath);
                            userPhoto.invalidate();
                            userPhoto.setImageURI(Uri.parse(imagePath));
                        }
                    }).execute();
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void loadProfilePicture() {
        if (this.sharedPreferencesContainer.getProfilePictureLocalPath() != "") {
            userPhoto.setImageURI(Uri.parse(this.sharedPreferencesContainer.getProfilePictureLocalPath()));
        } else {

            if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            } else {

                new ProfilePictureTask(this.getContext(), sharedPreferencesContainer.getProfilePictureUrl(), new ProfilePictureTaskResponse() {
                    @Override
                    public void OnImageAvailable(@NonNull String imagePath) {
                        sharedPreferencesContainer.setProfilePictureLocalPath(imagePath);
                        userPhoto.invalidate();
                        userPhoto.setImageURI(Uri.parse(imagePath));
                    }
                }).execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(SettingsFragment.class.getName(), "onResume");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terms_text:
                firebaseAnalytics.logEvent("terms_and_conditions", null);
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_text:
                firebaseAnalytics.logEvent("feedback", null);
                this.eMailService.SendFeedbackEMail("");
                break;
            case R.id.onBoarding_text:
                firebaseAnalytics.logEvent("onboarding", null);
                Intent intent1 = new Intent(getActivity(), OnBoardingActivity.class);
                startActivity(intent1);
                break;
            case R.id.history_text:
                firebaseAnalytics.logEvent("alarm_history", null);
                Intent i = new Intent(getActivity(), AlarmHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.logout_text:
                firebaseAnalytics.logEvent("logout", null);
                showLogoutPopup();
                break;
            case R.id.payment_text:
                firebaseAnalytics.logEvent("settings_payments", null);
                displayPayment();
                break;
            case R.id.profile_text:
                firebaseAnalytics.logEvent("settings_profile", null);
                Intent intent2 = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void displayPayment() {
        loadingSpinner.setVisibility(View.VISIBLE);
        Window currentWindow = getActivity().getWindow();
        if (currentWindow != null) {
            currentWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            currentWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        this.braintreePaymentInteractor.displayPaymentMethods(new OnPaymentInteractionResult() {
            @Override
            public void onSuccess() {

                sharedPreferencesContainer.setFreeCredits(0);
                dismissLoadingCircle();
            }

            @Override
            public void onCanceled() {
                dismissLoadingCircle();
            }
        });
    }

    private void showLogoutPopup() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.logout_popup);
        TextView yes = dialog.findViewById(R.id.yes_textview);
        TextView cancel = dialog.findViewById(R.id.cancel_text);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationService.socialLogout();

                dialog.dismiss();
                //do this on logout button click
                Intent intent = new Intent(Constants.LOG_OUT);
                //send the broadcast to all activities who are listening
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
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

    private void dismissLoadingCircle() {
        loadingSpinner.setVisibility(View.INVISIBLE);
        Window currentWindow = getActivity().getWindow();
        if (currentWindow != null) {
            currentWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    //--- PAYMENT
    private void canShowGooglePayButton() {
        final JSONObject isReadyToPayJson = GooglePaymentsUtil.getIsReadyToPayRequest();

        if (isReadyToPayJson == null) {
            return;
        }

        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
        if (request == null) {
            return;
        }

        Task<Boolean> task = GooglePaymentsUtil.createPaymentsClient(requireActivity()).isReadyToPay(request);
        task.addOnCompleteListener(requireActivity(),
                availabilityTask -> {
                    if (availabilityTask.isSuccessful()) {
                        setGooglePayAvailable(Objects.requireNonNull(availabilityTask.getResult()));
                    } else {
                        Log.e(TAG, "Payment through Google Pay cannot be started:\n" + availabilityTask.getException());
                    }
                });
    }

    private void setGooglePayAvailable(boolean isAvailable) {
        if (isAvailable) {
            googlePayBtn.setVisibility(View.VISIBLE);

            googlePayBtn.setOnClickListener(v -> startPayment());
        } else {
            // TODO flow if Google Pay is not available
            googlePayBtn.setVisibility(View.GONE);
        }
    }

    private void startPayment() {
        String price = "1"; // TODO use real price

        JSONObject paymentDataRequestJson = GooglePaymentsUtil.getPaymentDataRequest(price);
        if (paymentDataRequestJson == null) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request), requireActivity(), GOOGLE_PAY_REQUEST_CODE);
        }

        // disable the button until payment is completed
        googlePayBtn.setClickable(false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);

                    onPaymentSuccess(paymentData);

                    break;
                case Activity.RESULT_CANCELED:
                    // user cancelled the payment
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);

                    onPaymentError(status != null ? status.getStatusCode() : -1);

                    break;
            }
        } else {
            braintreePaymentInteractor.onActivityResult(requestCode, resultCode, data);
        }

        // re-enable gpay button
        googlePayBtn.setClickable(true);
    }

    private void onPaymentSuccess(PaymentData paymentData) {
        //TODO flow if Google Pay payment was successful
        if (BuildConfig.DEBUG) {
            Toast.makeText(requireContext(), "Master gave Dobby money; Dobby is a free elf", Toast.LENGTH_LONG).show();
        }

        //TODO add real number of credits
        int credits = sharedPreferencesContainer.getFreeCredits();
        credits += 50;
        sharedPreferencesContainer.setFreeCredits(credits);
        this.cashTextView.setText(credits + " $");


        // obtain payment info
        String paymentInformation = paymentData.toJson();

        if (paymentInformation == null) {
            return;
        }

        JSONObject paymentMethodData;

        try {
            paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
            // ENV_TEST - dummy token is "examplePaymentMethodToken" for "example" merchant

            JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            String token = tokenizationData.getString("token");
            Log.d(TAG, "token obtained = " + token);


            JSONObject billingData = paymentMethodData.getJSONObject("info");
            String cardNetwork = billingData.getString("cardNetwork");
            String cardLastDigits = billingData.getString("cardDetails");
            Log.d(TAG, "card = " + cardNetwork + cardLastDigits);
        } catch (Exception e) {
            Log.e(TAG, "Error handling payment success");
            e.printStackTrace();
        }
    }

    private void onPaymentError(int statusCode) {
        //TODO flow if Google Pay payment failed

        Log.e(TAG, "Payment failed with status code " + statusCode);
    }
    //------ END PAYMENT
}
