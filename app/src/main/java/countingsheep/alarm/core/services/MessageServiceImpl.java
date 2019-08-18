package countingsheep.alarm.core.services;

import android.app.Activity;

import androidx.work.impl.constraints.NetworkState;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.MessageRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.NetworkStateInteractor;
import countingsheep.alarm.infrastructure.NetworkStateReceiver;
import countingsheep.alarm.infrastructure.NotificationHelper;
import countingsheep.alarm.network.retrofit.MessageAPI;
import io.fabric.sdk.android.services.common.Crash;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class MessageServiceImpl  implements MessageService {


    private MessageRepository messageRepository;
    private TimeService timeService;
    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private NetworkStateInteractor networkStateInteractor;

    @Inject
    public MessageServiceImpl(MessageRepository messageRepository, TimeService timeService,
                              Retrofit retrofit, SharedPreferencesContainer sharedPreferencesContainer,
                              NetworkStateInteractor networkStateInteractor) {
        this.messageRepository = messageRepository;
        this.timeService = timeService;
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkStateInteractor = networkStateInteractor;
    }

    public void add(Message message, OnAsyncResponse<Long> onAsyncResponse){
        try{
            this.messageRepository.insert(message, onAsyncResponse);
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void markSeen(int messageId) {
        Message message = this.messageRepository.get(messageId);
        message.setSeen(true);
        message.setSeenAt(this.timeService.getUTCDateNow());
        this.messageRepository.update(message);
    }

    @Override
    public Message getUnseenMessage() {
        return this.messageRepository.getNotSeen();
    }

    @Override
    public void markMessageLiked(int messageId) {
        Message message = this.messageRepository.get(messageId);
        message.setLiked(true);
        this.messageRepository.update(message);
    }

    @Override
    public void markMessageShared(int messageId) {
        try {
            Message message = this.messageRepository.get(messageId);
            message.setShared(true);
            this.messageRepository.update(message);
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public List<Message> getAllUnsynced() {
        //TODO:: implement everything using try catch + logging
        return this.messageRepository.getAllUnsynced(true);
    }

    @Override
    public boolean markSyncedRange(List<Message> unsyncedMessages) {
        try {

            List<Integer> messageIds = new ArrayList<>();
            for (Message message: unsyncedMessages) {
                messageIds.add(message.getId());
            }

            this.messageRepository.markMessagesSynced(messageIds);

            return true;
        } catch (Exception exception) {
            Crashlytics.logException(exception);
            return false;
        }
    }

    @Override
    public void getRoastMessageHistory(OnAsyncResponse<List<Message>> onAsyncResponse){
        try{
            this.messageRepository.getAllHistory(onAsyncResponse);
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }


    @Override
    public void getRoastMessage(OnResult<Message> messageOnResult){
        if(this.networkStateInteractor.isNetworkAvailable()) {
            this.retrofit.create(MessageAPI.class).getRoastMessage(this.sharedPreferencesContainer.getCurrentUserId()).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        messageOnResult.onSuccess(response.body());
                    } else {
                        messageOnResult.onFailure();
                        sharedPreferencesContainer.increaseNeededToRequestRoastCount();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    sharedPreferencesContainer.increaseNeededToRequestRoastCount();
                    messageOnResult.onFailure();
                    Crashlytics.logException(t);
                }
            });
        }
        else{
            this.sharedPreferencesContainer.increaseNeededToRequestRoastCount();
            messageOnResult.onFailure("Please connect to the internet to receive a roast.");
        }
    }

    @Override
    public NetworkStateReceiver.NetworkStateReceiverListener getRoastMessageNetworkStateListener(Activity activity) {
        return new NetworkStateReceiver.NetworkStateReceiverListener() {
            @Override
            public void onNetworkAvailable() {
                int requiredRoastsCount = sharedPreferencesContainer.getNeedToRequestRoastCount();
                if(requiredRoastsCount == 0)
                    return;

                sharedPreferencesContainer.resetNeededToRequestRoastCount();

                for (int index = 1; index<= requiredRoastsCount;index++){
                    getRoastMessage(new OnResult<Message>() {
                        @Override
                        public void onSuccess(Message result) {
                            NotificationHelper notificationHelper = new NotificationHelper(activity);
                            notificationHelper.displayNotification("Your roast is here!!", "");
                            messageRepository.insert(result);
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
