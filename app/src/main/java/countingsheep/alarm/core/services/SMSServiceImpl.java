package countingsheep.alarm.core.services;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.domain.SMSRoastMessageReq;
import countingsheep.alarm.core.domain.SavePhoneNoReq;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.NetworkStateInteractor;
import countingsheep.alarm.infrastructure.NetworkStateReceiver;
import countingsheep.alarm.network.retrofit.SMSAPI;
import countingsheep.alarm.network.retrofit.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class SMSServiceImpl implements SMSService {

    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private NetworkStateInteractor networkStateInteractor;

    private NetworkStateReceiver networkStateReceiver;

    @Inject
    SMSServiceImpl(Retrofit retrofit,
                   SharedPreferencesContainer sharedPreferencesContainer,
                   NetworkStateInteractor networkStateInteractor){
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkStateInteractor = networkStateInteractor;
    }

    @Override
    public void sendToSelf(OnResult onResult) {

        if(this.networkStateInteractor.isNetworkAvailable()) {
            SMSRoastMessageReq smsRoastMessageReq = new SMSRoastMessageReq();
            smsRoastMessageReq.setUserId(sharedPreferencesContainer.getCurrentUserId());
            smsRoastMessageReq.setPhoneNumber(sharedPreferencesContainer.getCurrentUserPhoneNumber());
            this.retrofit.create(SMSAPI.class).sendRoastSMS(smsRoastMessageReq).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (onResult != null) {
                        onResult.onSuccess(null);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Crashlytics.logException(t);
                    if (onResult != null) {
                        onResult.onFailure();
                    }
                }
            });
        }
        else{
            this.sharedPreferencesContainer.increaseNeededToRequestSMSRoastCount();

            if (onResult != null) {
                onResult.onFailure("Make sure you are connected to internet in order to receive the roast!");
            }
        }
    }

    //TODO:: save phoneNo when connected to internet
    @Override
    public void savePhoneNumber(String phoneNoInput) {
        if(this.networkStateInteractor.isNetworkAvailable()) {
            SavePhoneNoReq savePhoneNoReq = new SavePhoneNoReq();
            savePhoneNoReq.setUserId(sharedPreferencesContainer.getCurrentUserId());
            savePhoneNoReq.setPhoneNo(sharedPreferencesContainer.getCurrentUserPhoneNumber());
            this.retrofit.create(UserAPI.class).savePhoneNumber(savePhoneNoReq).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Crashlytics.logException(t);
                }
            });
        }
    }

    @Override
    public NetworkStateReceiver.NetworkStateReceiverListener getSMSNetworkStateListener() {
        return new NetworkStateReceiver.NetworkStateReceiverListener() {
            @Override
            public void onNetworkAvailable() {
                int requiredRoastsCount = sharedPreferencesContainer.getNeedToRequestSMSRoastCount();
                if(requiredRoastsCount == 0)
                    return;

                sharedPreferencesContainer.resetNeededToRequestSMSRoastCount();

                for (int index = 1; index<= requiredRoastsCount;index++){
                    sendToSelf(new OnResult() {
                        @Override
                        public void onSuccess(Object result) {

                        }
                        @Override
                        public void onFailure(String message) {
                            sharedPreferencesContainer.increaseNeededToRequestSMSRoastCount();
                        }
                    });
                }
            }

            @Override
            public void onNetworkUnavailable() {

            }
        };
    }
}
