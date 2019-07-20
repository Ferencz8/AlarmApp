package countingsheep.alarm.core.services;

import com.crashlytics.android.Crashlytics;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.domain.SMSRoastMessageReq;
import countingsheep.alarm.core.domain.SMSScheduledRoast;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.NetworkInteractor;
import countingsheep.alarm.network.retrofit.SMSAPI;
import io.fabric.sdk.android.services.common.Crash;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class SMSServiceImpl implements SMSService {

    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private NetworkInteractor networkInteractor;
    private TimeService timeService;

    @Inject
    SMSServiceImpl(Retrofit retrofit,
                   SharedPreferencesContainer sharedPreferencesContainer,
                   NetworkInteractor networkInteractor,
                   TimeService timeService){
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkInteractor = networkInteractor;
        this.timeService = timeService;
    }

    @Override
    public void sendToSelf(OnResult onResult) {

        if(this.networkInteractor.isNetworkAvailable()) {
            SMSRoastMessageReq smsRoastMessageReq = new SMSRoastMessageReq();
            smsRoastMessageReq.setUserId(sharedPreferencesContainer.getCurrentUserId());
            smsRoastMessageReq.setPhoneNumber(sharedPreferencesContainer.getCurrentUserPhoneNumber());//TODO this requires manual input from the user to add his / her phone number
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
            if (onResult != null) {
                onResult.onFailure("Not connected to internet");
            }
        }
    }

    @Override
    public void sendToFriend(int messageId, String recipientPhoneNo, Date scheduledDate, OnResult onResult){
        if(this.networkInteractor.isNetworkAvailable()) {
            SMSScheduledRoast smsScheduledRoast = new SMSScheduledRoast();
            smsScheduledRoast.setDateCreated(this.timeService.getUTCDateNow());
            smsScheduledRoast.setSenderUserId(this.sharedPreferencesContainer.getCurrentUserId());
            smsScheduledRoast.setMessageId(messageId);
            smsScheduledRoast.setRecipientPhoneNo(recipientPhoneNo);
            smsScheduledRoast.setScheduledDate(scheduledDate);

            retrofit.create(SMSAPI.class).sendRoastSMSToFriend(smsScheduledRoast).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(onResult!=null) {
                        onResult.onSuccess(null);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (onResult != null) {
                        onResult.onFailure();
                    }
                    Crashlytics.logException(t);
                }
            });
        }
        else{
            if (onResult != null) {
                onResult.onFailure("Not connected to internet");
            }
        }
    }
}
