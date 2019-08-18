package countingsheep.alarm.core.services.interfaces;

import android.app.Activity;

import java.util.List;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.infrastructure.NetworkStateReceiver;

public interface MessageService {

    void markSeen(int messageId);

    Message getUnseenMessage();

    void markMessageLiked(int messageId);

    void markMessageShared(int messageId);

    List<Message> getAllUnsynced();

    boolean markSyncedRange(List<Message> unsyncedMessages);

    NetworkStateReceiver.NetworkStateReceiverListener getRoastMessageNetworkStateListener(Activity activity);

    void getRoastMessage(OnResult<Message> messageOnResult);

    void getRoastMessageHistory(OnAsyncResponse<List<Message>> onAsyncResponse);

    void add(Message message, OnAsyncResponse<Long> onAsyncResponse);
}
