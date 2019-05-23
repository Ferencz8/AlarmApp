//package countingsheep.alarm.ui.card;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.braintreepayments.api.BraintreeFragment;
//import com.braintreepayments.api.Card;
//import com.braintreepayments.api.DataCollector;
//import com.braintreepayments.api.dropin.DropInRequest;
//import com.braintreepayments.api.exceptions.InvalidArgumentException;
//import com.braintreepayments.api.interfaces.BraintreeErrorListener;
//import com.braintreepayments.api.interfaces.BraintreeResponseListener;
//import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
//import com.braintreepayments.api.models.CardBuilder;
//import com.braintreepayments.api.models.PaymentMethodNonce;
//import com.braintreepayments.cardform.OnCardFormFieldFocusedListener;
//import com.braintreepayments.cardform.OnCardFormSubmitListener;
//import com.braintreepayments.cardform.utils.CardType;
//import com.braintreepayments.cardform.view.CardForm;
//import com.google.android.material.textfield.TextInputLayout;
//
//import javax.inject.Inject;
//
//import androidx.appcompat.app.AppCompatActivity;
//import countingsheep.alarm.Injector;
//import countingsheep.alarm.R;
//import countingsheep.alarm.core.domain.Checkout;
//import countingsheep.alarm.core.domain.Customer;
//import countingsheep.alarm.network.retrofit.PaymentAPI;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//
//public class CardActivity extends AppCompatActivity implements OnCardFormSubmitListener, BraintreeErrorListener {
//
//    private CardForm mCardForm;
//    private TextInputLayout mSmsCodeContainer;
//
//    private Button mPurchaseButton;
//    private Button buyBtn;
//    private BraintreeFragment mBraintreeFragment;
//
//    @Inject()
//    Retrofit retrofit;
//    private String tokenToPay;
//
//
//    @Override
//    protected void onCreate(Bundle onSaveInstanceState) {
//        super.onCreate(onSaveInstanceState);
//
//        setContentView(R.layout.card_activity);
//        Injector.getActivityComponent(this).inject(this);
//
//        final AppCompatActivity activity = this;
//        mCardForm = findViewById(R.id.card_form);
//        mCardForm.setOnCardFormSubmitListener(this);
//
//
//        mCardForm.cardRequired(true)
//                .expirationRequired(true)
//                .cvvRequired(true)
//                .mobileNumberRequired(false)
//                .cardholderName(CardForm.FIELD_REQUIRED)
//                .actionLabel("Save")
//                .setup(this);
//
//
//        mPurchaseButton = findViewById(R.id.purchase_button);
//buyBtn = findViewById(R.id.buy_button);
//        retrofit.create(PaymentAPI.class).generateToken().enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                if (response.isSuccessful()) {
//
//                    try {
//                        mBraintreeFragment = BraintreeFragment.newInstance(activity, response.body());
//                        mBraintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
//                            @Override
//                            public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//                                String nonce = paymentMethodNonce.getNonce();
//                                Checkout checkout = new Checkout();
//                                 retrofit.create(PaymentAPI.class).register(null).enqueue(new Callback<Customer>() {
//                                    @Override
//                                    public void onResponse(Call<Customer> call, Response<Customer> response) {
//
//                                        if (response.isSuccessful()) {
//                                            Log.d("Yay", "Payment went smoothly");
//                                            tokenToPay = response.body().getToken();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<Customer> call, Throwable t) {
//                                        Log.d("ERROR", "Payment Failed");
//                                    }
//                                });
//                            }
//                        });
//                    } catch (InvalidArgumentException e) {
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("ERROR", "Failed AR");
//            }
//        });
//
////        DataCollector.collectDeviceData(mBraintreeFragment, new BraintreeResponseListener<String>() {
////            @Override
////            public void onResponse(String deviceData) {
////                mDeviceData = deviceData;
////            }
////        });
//
//
//    }
//
//
//    @Override
//    public void onCardFormSubmit() {
//        onPurchase(null);
//    }
//
//    public void onPurchase(View v) {
//
//        CardBuilder cardBuilder = new CardBuilder()
//                .cardholderName(mCardForm.getCardholderName())
//                .cardNumber(mCardForm.getCardNumber())
//                .expirationMonth(mCardForm.getExpirationMonth())
//                .expirationYear(mCardForm.getExpirationYear())
//                .cvv(mCardForm.getCvv());
//
//        Card.tokenize(mBraintreeFragment, cardBuilder);
//
//    }
//
//    public void onBuy(View v){
//
//        Checkout checkout = new Checkout();
//        checkout.setPrice(4);
//        checkout.setToken("f4gp6q");//tokenToPay);
//
//        retrofit.create(PaymentAPI.class).checkout(checkout).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//                if (response.isSuccessful()) {
//                    Log.d("Yay", "Payment went smoothly");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("ERROR", "Payment Failed");
//            }
//        });
//    }
//
//    @Override
//    public void onError(Exception error) {
//        Log.d("CARDACTIVITY", error.getMessage());
//    }
//}