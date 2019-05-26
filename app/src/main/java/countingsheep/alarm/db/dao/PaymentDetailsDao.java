package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.PaymentDetails;


@Dao
public interface PaymentDetailsDao extends BaseDao<PaymentDetails> {

    @Query("SELECT * FROM PaymentDetails WHERE paymentStatus =:paymentStatus")
    List<PaymentDetails> getAll(int paymentStatus);

    @Query("SELECT * FROM PaymentDetails WHERE alarmReactionId =:alarmReactionId")
    PaymentDetails getForAlarmReactionId(int alarmReactionId);
}
