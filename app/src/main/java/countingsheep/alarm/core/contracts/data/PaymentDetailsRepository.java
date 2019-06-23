package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;

public interface PaymentDetailsRepository {

    void insert(PaymentDetails paymentDetails);

    void update(PaymentDetails paymentDetails);

    void getAll(PaymentStatus paymentStatus, OnAsyncResponse<List<PaymentDetails>> onAsyncResponse);

    void getForAlarmReactionId(int alarmReactionId, OnAsyncResponse<PaymentDetails> onAsyncResponse);

    void getSumAmount(OnAsyncResponse<Integer> response);
}
