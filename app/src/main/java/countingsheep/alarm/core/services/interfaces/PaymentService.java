package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.OnResult;

public interface PaymentService {

    void processPayment(int alarmReactionId, OnResult onResult);
}
