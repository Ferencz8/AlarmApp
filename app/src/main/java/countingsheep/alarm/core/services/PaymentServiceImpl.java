package countingsheep.alarm.core.services;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.domain.Checkout;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.NetworkInteractor;
import countingsheep.alarm.network.retrofit.PaymentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class PaymentServiceImpl implements PaymentService {

    private String TAG = this.getClass().getName();

    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private NetworkInteractor networkInteractor;

    @Inject
    PaymentServiceImpl(Retrofit retrofit,
                       SharedPreferencesContainer sharedPreferencesContainer,
                       NetworkInteractor networkInteractor) {
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkInteractor = networkInteractor;
    }


    @Override
    public void processPayment(int alarmReactionId, OnResult onResult) {

        if (alarmReactionId == 0) return;

        try {
            Checkout checkout = new Checkout();
            checkout.setToken(this.sharedPreferencesContainer.getToken());
            checkout.setPrice(this.sharedPreferencesContainer.getDefaultSnoozePrice());
            checkout.setAlarmReactionId(alarmReactionId);

            if (!this.networkInteractor.isNetworkAvailable()) {
                onResult.onFailure();
            }
            else{

                retrofit.create(PaymentAPI.class).checkout(checkout).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "Payment processed successfully");
                            onResult.onSuccess(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "Payment processing Failed");
                        onResult.onFailure();
                    }
                });
            }
        } catch (Exception exception) {
            Log.e(TAG, "Failed to process payment with exception message" + exception.getMessage());
        }
    }
}
