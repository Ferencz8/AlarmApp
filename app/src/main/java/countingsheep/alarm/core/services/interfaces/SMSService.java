package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.domain.SMSRoastMessageReq;

public interface SMSService {

    void sendToSelf(OnResult onResult);

    void savePhoneNumber(String phoneNoInput);
}
