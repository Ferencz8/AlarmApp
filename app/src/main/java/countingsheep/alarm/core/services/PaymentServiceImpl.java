package countingsheep.alarm.core.services;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.domain.Checkout;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
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
    private PaymentDetailsRepository paymentDetailsRepository;
    private TimeService timeService;

    @Inject
    PaymentServiceImpl(Retrofit retrofit,
                       SharedPreferencesContainer sharedPreferencesContainer,
                       NetworkInteractor networkInteractor,
                       PaymentDetailsRepository paymentDetailsRepository,
                       TimeService timeService) {
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkInteractor = networkInteractor;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.timeService = timeService;
    }


    @Override
    public void processPayment(int alarmReactionId, OnResult onResult) {

        if (alarmReactionId == 0) return;
        PaymentDetails paymentDetails = getPaymentDetails(alarmReactionId);

        try {
            Checkout checkout = initializeCheckout(alarmReactionId);

            if (!this.networkInteractor.isNetworkAvailable()) {
                InsertPaymentDetail(paymentDetails, PaymentStatus.Failed);
                if(onResult!=null) {
                    onResult.onFailure();
                }
            }
            else{
                retrofit.create(PaymentAPI.class).checkout(checkout).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "PaymentDetails processed successfully");

                            paymentDetails.setTransactionId(response.body());
                            InsertPaymentDetail(paymentDetails, PaymentStatus.Requested);
                            if(onResult!=null){
                                onResult.onSuccess(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "PaymentDetails processing Failed");

                        InsertPaymentDetail(paymentDetails, PaymentStatus.Failed);
                        if(onResult!=null) {
                            onResult.onFailure();
                        }
                    }
                });
            }
        } catch (Exception exception) {
            Log.e(TAG, "Failed to process payment with exception message" + exception.getMessage());
            InsertPaymentDetail(paymentDetails, PaymentStatus.Failed);
        }
    }


//    public void processFailedPayment(int alarmReactionId, OnResult<String> onResult){
//        if (alarmReactionId == 0) return;
//        try {
//            if (!this.networkInteractor.isNetworkAvailable()) {
//                onResult.onFailure();
//            }
//            else {
//
//                Checkout checkout = initializeCheckout(alarmReactionId);
//
//                this.paymentDetailsRepository.getForAlarmReactionId(alarmReactionId, new OnAsyncResponse<PaymentDetails>() {
//                    @Override
//                    public void processResponse(PaymentDetails response) {
//                        if(response.getPaymentStatus() != PaymentStatus.Failed){
//                            //TODO log -> FATAL ERROR
//                        }
//                        else{
//
//                        }
//                    }
//                });
//
//
//                retrofit.create(PaymentAPI.class).checkout(checkout).enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        if (response.isSuccessful()) {
//                            Log.d(TAG, "PaymentDetails processed successfully");
//
//                            paymentDetails.setTransactionId(response.body());
//                            InsertPaymentDetail(paymentDetails, PaymentStatus.Requested);
//                            if (onResult != null) {
//                                onResult.onSuccess(null);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Log.d(TAG, "PaymentDetails processing Failed");
//
//                        InsertPaymentDetail(paymentDetails, PaymentStatus.Failed);
//                        if (onResult != null) {
//                            onResult.onFailure();
//                        }
//                    }
//                });
//            }
//        } catch (Exception exception) {
//            Log.e(TAG, "Failed to process payment with exception message" + exception.getMessage());
//            InsertPaymentDetail(paymentDetails, PaymentStatus.Failed);
//        }
//    }

    private void InsertPaymentDetail(PaymentDetails paymentDetails, PaymentStatus status) {
        paymentDetails.setPaymentStatus(status);
        paymentDetailsRepository.insert(paymentDetails);
    }

    private Checkout initializeCheckout(int alarmReactionId) {
        Checkout checkout = new Checkout();
        checkout.setToken(this.sharedPreferencesContainer.getToken());
        checkout.setPrice(this.sharedPreferencesContainer.getDefaultSnoozePrice());
        checkout.setAlarmReactionId(alarmReactionId);
        return checkout;
    }

    private PaymentDetails getPaymentDetails(int alarmReactionId) {

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAmount(sharedPreferencesContainer.getDefaultSnoozePrice());
        paymentDetails.setDateCreated(timeService.getUTCDateNow());
        paymentDetails.setDateModified(timeService.getUTCDateNow());
        paymentDetails.setAlarmReactionId(alarmReactionId);
        return paymentDetails;
    }
}
