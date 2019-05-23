package countingsheep.alarm.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.cardform.view.CardForm;

import javax.inject.Inject;

import countingsheep.alarm.core.domain.Customer;
import countingsheep.alarm.core.domain.CustomerRegistration;
import countingsheep.alarm.core.domain.PaymentRegistration;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.network.retrofit.PaymentAPI;
import countingsheep.alarm.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BraintreePaymentInteractor {

    private String TAG = this.getClass().getName();

    private DropInRequest dropInRequest;
    private Retrofit retrofit;
    private Activity activity;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private OnPaymentInteractionResult onPaymentInteractionResult;

    @Inject
    public BraintreePaymentInteractor(Activity activity,
                                      Retrofit retrofit,
                                      SharedPreferencesContainer sharedPreferencesContainer){
        this.retrofit = retrofit;
        this.activity = activity;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
    }

    public void displayPaymentMethods(OnPaymentInteractionResult onPaymentInteractionResult){

        this.onPaymentInteractionResult = onPaymentInteractionResult;
        retrofit.create(PaymentAPI.class).generateToken(sharedPreferencesContainer.getCustomerId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    dropInRequest = new DropInRequest()
                            .disablePayPal()
                            .vaultManager(true)
                            .cardholderNameStatus(CardForm.FIELD_REQUIRED)
                            .clientToken(response.body());

                    activity.startActivityForResult(dropInRequest.getIntent(activity), Constants.PaymentRequestCode);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.d("ERROR", "Failed AR");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode!=Constants.PaymentRequestCode) return;

        if (resultCode == Activity.RESULT_OK) {
            DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

            //todo:: decide if it is a new customer/a new payent method/or none of the above
            String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();

            String customerId = this.sharedPreferencesContainer.getCustomerId();
            if(customerId== null || customerId.isEmpty()){
                addOrUpdatePaymentMethod(paymentMethodNonce, this.sharedPreferencesContainer.getCurrentUserId(), customerId);
                //registerCustomer(paymentMethodNonce, this.sharedPreferencesContainer.getCurrentUserId());
            }
            else {
                addOrUpdatePaymentMethod(paymentMethodNonce, this.sharedPreferencesContainer.getCurrentUserId(), customerId);
            }

            this.onPaymentInteractionResult.onSuccess();
            // send paymentMethodNonce to your server
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // canceled
            this.onPaymentInteractionResult.onCanceled();
        } else {
            // an error occurred, checked the returned exception
            Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
        }
    }

    private void addOrUpdatePaymentMethod(String paymentMethodNonce, int currentUserId, String customerId) {

        PaymentRegistration paymentRegistration = new PaymentRegistration();
        paymentRegistration.setCustomerId(customerId);
        paymentRegistration.setPaymentMethodNonce(paymentMethodNonce);
        paymentRegistration.setUserId(currentUserId);

        retrofit.create(PaymentAPI.class).addOrUpdatePaymentMethod(paymentRegistration).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Customer registered successfully");
                    sharedPreferencesContainer.setToken(response.body() );
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Customer registration failed");
            }
        });
    }

    private void registerCustomer(String nonce, int userId){
        CustomerRegistration customerRegistration = new CustomerRegistration();
        customerRegistration.setPaymentMethodNonce(nonce);
        customerRegistration.setUserId(userId);

        retrofit.create(PaymentAPI.class).register(customerRegistration).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Customer registered successfully");
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e(TAG, "Customer registration failed");
            }
        });
    }
}
