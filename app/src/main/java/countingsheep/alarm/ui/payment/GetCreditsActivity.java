package countingsheep.alarm.ui.payment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.Injector;
import countingsheep.alarm.MainActivity;
import countingsheep.alarm.R;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.CreditsPackage;
import countingsheep.alarm.ui.BaseActivity;
import countingsheep.alarm.ui.freecredits.FreeCreditsActivity;

public class GetCreditsActivity extends BaseActivity
        implements CreditsPackagesAdapter.CreditsPackageListener, PurchasesUpdatedListener, ConsumeResponseListener {
    private static final String TAG = "GetCreditsActivity";
    protected FirebaseAnalytics firebaseAnalytics;

    private RecyclerView packagesGrid;
    private Button getPackage;

    private ConstraintLayout headerBar;
    private ImageView backBtn;
    private TextView titleTv;

    public static final int GOOGLE_PAY_REQUEST_CODE = 100;
    private CreditsPackage selectedPackage;
    private BillingClient billingClient;
    private List<SkuDetails> skuDetailsList;

    @Inject
    SharedPreferencesContainer sharedPreferencesContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_credits);

        bindViews();
        initPayment();

        Injector.getActivityComponent(this).inject(GetCreditsActivity.this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent("getCredits",null);
    }

    private void bindViews() {
        packagesGrid = findViewById(R.id.packagesGrid);
        getPackage = findViewById(R.id.getPackage);

        headerBar = findViewById(R.id.headerBar);
        backBtn = headerBar.findViewById(R.id.backBtn);
        titleTv = headerBar.findViewById(R.id.titleTv);
        Typeface bold_font = Typeface.createFromAsset(this.getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        titleTv.setTypeface(bold_font);
        titleTv.setText(R.string.get_credits);
        backBtn.setOnClickListener(v -> {
                finish();
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
    private int billingConnectionRetries = 0;
    private void initPayment() {
        billingClient = BillingClient
                .newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // BillingClient is ready
                    updateProductList();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // BillingClient disconnected; try to restart the connection
                if (billingConnectionRetries < 3) {
                    billingConnectionRetries += 1;

                    initPayment();
                }
            }
        });
    }

    private void updateProductList() {
        List<String> skuList = new ArrayList<>();
        skuList.add("5credits"); skuList.add("25credits");
        skuList.add("50credits"); skuList.add("eternalcredits");

        // for testing purposes; TODO remove
//        if (BuildConfig.DEBUG) {
//            skuList.add("android.test.purchased");
//            skuList.add("android.test.canceled");
//            skuList.add("android.test.item_unavailable");
//        }

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, skuDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                this.skuDetailsList = skuDetailsList;
            } else {
                Log.e(TAG, "There are no credits packages defined in dev console");

                Toast.makeText(this, getString(R.string.error_no_credits_package_available), Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void startPayment(CreditsPackage selectedPackage) {
        if (skuDetailsList == null || skuDetailsList.size() == 0) {
            Log.e(TAG, "The list of credits packages is empty");

            Toast.makeText(this, getString(R.string.error_no_credits_package_available), Toast.LENGTH_LONG).show();

            return;
        }

        String selectedSku = "";
        switch (selectedPackage.getCredits()) {
            case 5: selectedSku = "5credits"; break;
            case 25: selectedSku = "25credits"; break;
            case 50: selectedSku = "50credits"; break;
            case 1000: selectedSku = "eternalcredits"; break;
        }

        for (SkuDetails skuDetail: skuDetailsList) {
            // for testing purposes; TODO remove
//            if (BuildConfig.DEBUG) {
//                if (selectedSku.equals("5credits")) {
//                    selectedSku = "android.test.purchased";
//                }
//                if (selectedSku.equals("25credits")) {
//                    selectedSku = "android.test.canceled";
//                }
//                if (selectedSku.equals("50credits")) {
//                    selectedSku = "android.test.item_unavailable";
//                }
//            }

            if (selectedSku.equals(skuDetail.getSku())) {
                launchBillingFlow(skuDetail);
                return;
            }
        }
    }

    private void launchBillingFlow(SkuDetails skuDetails) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        billingClient.launchBillingFlow(this, flowParams);
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                consumePurchase(purchase.getPurchaseToken());
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "User canceled the payment");
        } else {
            Log.e(TAG, "Payment failed with status code " + billingResult.getResponseCode());

            Toast.makeText(this, getString(R.string.payment_error), Toast.LENGTH_LONG).show();
        }
    }

    private void consumePurchase(String purchaseToken) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();

        billingClient.consumeAsync(consumeParams, this);
    }

    @Override
    public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
        // payment was consumed; give credits to user
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

        Toast.makeText(this, getString(R.string.success_credits_updated), Toast.LENGTH_LONG).show();

        GetCreditsActivity.this.finish();
    }

    @Override
    public void onItemClick(CreditsPackage selectedPackage) {
        this.selectedPackage = selectedPackage;
    }
    //------ END PAYMENT
}
