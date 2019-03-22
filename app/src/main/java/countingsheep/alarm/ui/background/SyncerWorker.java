package countingsheep.alarm.ui.background;

import android.support.annotation.NonNull;

import java.util.List;

import androidx.work.Worker;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.network.retrofit.AlarmAPI;
import countingsheep.alarm.network.retrofit.MessageAPI;
import countingsheep.alarm.network.retrofit.UserWrappedEntities;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Why use this instead of AsynTask
 * https://expertise.jetruby.com/android-workmanager-the-future-is-coming-2b4bdd188050
 */
public class SyncerWorker extends Worker {

    private Retrofit retrofit;
    private AlarmService alarmService;
    private MessageService messageService;
    private SharedPreferencesContainer sharedPreferencesContainer;

    public SyncerWorker(Retrofit retrofit,
                        AlarmService alarmService,
                        MessageService messageService,
                        SharedPreferencesContainer sharedPreferencesContainer) {
        this.retrofit = retrofit;
        this.alarmService = alarmService;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
    }

    @NonNull
    @Override
    public WorkerResult doWork() {

        try{
            syncAlarms();

            return WorkerResult.SUCCESS;
        }
        catch (Exception exception){

            //TODO:LOG
            return WorkerResult.FAILURE;
        }
    }

    private void syncAlarms(){
        final List<Alarm> unsyncedAlarms = this.alarmService.getAllUnSynced();
        final UserWrappedEntities<Alarm> alarmsWrapped = new UserWrappedEntities<Alarm>();
        alarmsWrapped.setEntities(unsyncedAlarms);
        int userId = this.sharedPreferencesContainer.getCurrentUserId();
        alarmsWrapped.setUserId(userId);

        retrofit.create(AlarmAPI.class).addAlarmRange(alarmsWrapped).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO: improve by adding a counter. For 5 times try syncing then just mark it as synced if all attempts fail ?
                alarmService.markSyncedRange(unsyncedAlarms);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
}
