package countingsheep.alarm.core.services.interfaces;

import java.util.Date;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.domain.SMSRoastMessageReq;
import countingsheep.alarm.infrastructure.NetworkStateReceiver;

public interface SMSService {

    void sendToSelf(OnResult onResult);

    void sendToFriend(int messageId, String recipientPhoneNo, Date scheduledDate, OnResult onResult);
    void savePhoneNumber(String phoneNoInput);

    NetworkStateReceiver.NetworkStateReceiverListener getSMSNetworkStateListener();
}
