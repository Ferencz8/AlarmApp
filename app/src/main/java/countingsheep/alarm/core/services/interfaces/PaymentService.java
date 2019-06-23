package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;

public interface PaymentService {

    void processPayment(int alarmReactionId, OnResult onResult);

    void getSumAmount(OnAsyncResponse<Integer> response);
}
