package countingsheep.alarm.ui.background;

import androidx.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.work.Worker;

import com.crashlytics.android.Crashlytics;

import countingsheep.alarm.Injector;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
import countingsheep.alarm.network.retrofit.AlarmAPI;
import countingsheep.alarm.network.retrofit.AlarmReactionAPI;
import countingsheep.alarm.network.retrofit.MessageAPI;
import countingsheep.alarm.network.retrofit.PaymentAPI;
import countingsheep.alarm.network.retrofit.UserWrappedEntities;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Why use this instead of AsyncTask
 * https://expertise.jetruby.com/android-workmanager-the-future-is-coming-2b4bdd188050
 */
public class SyncerWorker extends Worker {

    private int userId;

    @Inject()
    Retrofit retrofit;

    @Inject()
    AlarmService alarmService;

    @Inject()
    AlarmReactionService alarmReactionService;

    @Inject()
    MessageService messageService;

    @Inject()
    SharedPreferencesContainer sharedPreferencesContainer;

    @Inject()
    PaymentDetailsRepository paymentDetailsRepository;


    @NonNull
    @Override
    public WorkerResult doWork() {

        Injector.getServiceComponent(this.getApplicationContext()).inject(this);

        userId = this.sharedPreferencesContainer.getCurrentUserId();
        if(userId==0) return WorkerResult.RETRY; //TODO:: CHECK MEANING, NEEDS SUCCESS?

        try{

            syncAlarms();

            syncAlarmReactions();

            //TODO:: maybe move in the future to a separate worker?
            //updatePaymentStatus();

            return WorkerResult.SUCCESS;
        }
        catch (Exception exception){

            Crashlytics.logException(exception);
            return WorkerResult.FAILURE;
        }
    }

    private void syncAlarmReactions() {

        final List<AlarmReaction> unsyncedAlarmsReactions = this.alarmReactionService.getAllUnsynced();

        if(unsyncedAlarmsReactions!=null && unsyncedAlarmsReactions.size() == 0)
            return;

        final UserWrappedEntities<AlarmReaction> alarmReactionWrapped = new UserWrappedEntities<AlarmReaction>();
        alarmReactionWrapped.setEntities(unsyncedAlarmsReactions);

        alarmReactionWrapped.setUserId(userId);

        retrofit.create(AlarmReactionAPI.class).addAlarmReactionRange(alarmReactionWrapped).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO: improve by adding a counter. For 5 times try syncing then just mark it as synced if all attempts fail ?
                alarmReactionService.markSyncedRange(unsyncedAlarmsReactions);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("ERROR", "Failed AR");
                Crashlytics.logException(t);
            }
        });
    }

    private void syncAlarms(){

        final List<Alarm> unsyncedAlarms = this.alarmService.getAllUnSynced();

        if(unsyncedAlarms!=null && unsyncedAlarms.size() == 0)
            return;

        final UserWrappedEntities<Alarm> alarmsWrapped = new UserWrappedEntities<Alarm>();
        alarmsWrapped.setEntities(unsyncedAlarms);

        alarmsWrapped.setUserId(userId);

        retrofit.create(AlarmAPI.class).addAlarmRange(alarmsWrapped).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //TODO: improve by adding a counter. For 5 times try syncing then just mark it as synced if all attempts fail ?
                alarmService.markSyncedRange(unsyncedAlarms);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("ERROR", "Failed A");
                Crashlytics.logException(t);
            }
        });
    }

    private void syncMessages(){
        final List<Message> unsyncedMessages = this.messageService.getAllUnsynced();
        final UserWrappedEntities<Message> messagesWrapped = new UserWrappedEntities<Message>();
        messagesWrapped.setEntities(unsyncedMessages);
        int userId = this.sharedPreferencesContainer.getCurrentUserId();
        messagesWrapped.setUserId(userId);

        retrofit.create(MessageAPI.class).markMessages(messagesWrapped).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO: improve by adding a counter. For 5 times try syncing then just mark it as synced if all attempts fail ?
                messageService.markSyncedRange(unsyncedMessages);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //TODO: log
            }
        });
    }

    private void updatePaymentStatus(){

        this.paymentDetailsRepository.getAll(PaymentStatus.Requested, new OnAsyncResponse<List<PaymentDetails>>() {
            @Override
            public void processResponse(List<PaymentDetails> response) {

                for (PaymentDetails paymentDetail: response) {
                    try {
                        retrofit.create(PaymentAPI.class).getPaymentStatus(paymentDetail.getTransactionId()).enqueue(new Callback<PaymentStatus>() {
                            @Override
                            public void onResponse(Call<PaymentStatus> call, Response<PaymentStatus> response) {
                                paymentDetail.setPaymentStatus(response.body());
                                paymentDetailsRepository.update(paymentDetail);
                            }

                            @Override
                            public void onFailure(Call<PaymentStatus> call, Throwable t) {
                                //TODO: log
                            }
                        });
                    }
                    catch(Exception ex){
                        Crashlytics.logException(ex);
                    }
                }
            }
        });
    }
}