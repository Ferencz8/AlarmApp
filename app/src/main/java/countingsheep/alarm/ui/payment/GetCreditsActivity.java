package countingsheep.alarm.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import countingsheep.alarm.BuildConfig;
import countingsheep.alarm.Injector;
import countingsheep.alarm.R;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.CreditsPackage;
import countingsheep.alarm.ui.BaseActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GetCreditsActivity extends BaseActivity implements CreditsPackagesAdapter.CreditsPackageListener {
    private static final String TAG = "GetCreditsActivity";
    protected FirebaseAnalytics firebaseAnalytics;

    private RecyclerView packagesGrid;
    private Button getPackage;

    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    private PaymentsClient paymentsClient;
    public static final int GOOGLE_PAY_REQUEST_CODE = 100;
    private CreditsPackage selectedPackage;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_credits);

        bindViews();

        Injector.getActivityComponent(this).inject(GetCreditsActivity.this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent("getCredits",null);
    }

    private void bindViews() {
        paymentsClient = GooglePaymentsUtil.createPaymentsClient(this);
        canShowGooglePayButton();

        packagesGrid = findViewById(R.id.packagesGrid);
        getPackage = findViewById(R.id.getPackage);

        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);
        Typeface bold_font = Typeface.createFromAsset(this.getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.get_credits);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        packagesGrid.setLayoutManager(new GridLayoutManager(this, 2));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        packagesGrid.addItemDecoration(new CreditsPackagesAdapter.CreditsPackageItemDecorator(spacingInPixels));

        CreditsPackagesAdapter adapter = new CreditsPackagesAdapter(this, this);
        packagesGrid.setAdapter(adapter);

        getPackage.setOnClickListener(v -> {
            if (this.selectedPackage == null) {
                Toast.makeText(GetCreditsActivity.this, getString(R.string.error_no_credits_package_selected), Toast.LENGTH_LONG).show();
            } else {
                startPayment(this.selectedPackage);
            }
        });
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

        Task<Boolean> task = GooglePaymentsUtil.createPaymentsClient(this).isReadyToPay(request);
        task.addOnCompleteListener(this,
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
            getPackage.setVisibility(VISIBLE);
        } else {
            // TODO flow if Google Pay is not available
            getPackage.setVisibility(GONE);
        }
    }

    private void startPayment(CreditsPackage selectedPackage) {
        JSONObject paymentDataRequestJson = GooglePaymentsUtil.getPaymentDataRequest(selectedPackage.getCost() + "");
        if (paymentDataRequestJson == null) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request), this, GOOGLE_PAY_REQUEST_CODE);
        }
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
        }
    }

    private void onPaymentSuccess(PaymentData paymentData) {
        //TODO flow if Google Pay payment was successful
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "Master gave Dobby money; Dobby is a free elf", Toast.LENGTH_LONG).show();
        }


        // obtain payment info
        String paymentInformation = paymentData.toJson();

        if (paymentInformation == null) {
            return;
        }

        if (selectedPackage != null) {
            if (selectedPackage.isEndless()) {
                //TODO sync with server and User entity
                sharedPreferencesContainer.setHasEndlessAccount(true);
            } else {
                int credits = sharedPreferencesContainer.getFreeCredits();
                credits += selectedPackage.getCredits();
                sharedPreferencesContainer.setFreeCredits(credits);
            }
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

        Toast.makeText(this, getString(R.string.success_credits_updated), Toast.LENGTH_LONG).show();

        GetCreditsActivity.this.finish();
    }

    private void onPaymentError(int statusCode) {
        //TODO flow if Google Pay payment failed

        Log.e(TAG, "Payment failed with status code " + statusCode);
    }

    @Override
    public void onItemClick(CreditsPackage selectedPackage) {
        this.selectedPackage = selectedPackage;
    }
    //------ END PAYMENT
}
