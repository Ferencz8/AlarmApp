package countingsheep.alarm.db.entities;

import androidx.room.Embedded;

import java.util.Date;

import countingsheep.alarm.ui.settings.models.AlarmHistory;

public class AlarmHistoryEmbedded extends DbEntity {

//    String name;
//    Date createdDate;
//    int amount;
//    int minute;
//    int hour;
//    boolean isSnooze;
//
//

    @Embedded
    Alarm alarm;

    @Embedded(prefix = "ar_")
    AlarmReaction alarmReaction;


    @Embedded(prefix = "pd_")
    PaymentDetails paymentDetails;

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public AlarmReaction getAlarmReaction() {
        return alarmReaction;
    }

    public void setAlarmReaction(AlarmReaction alarmReaction) {
        this.alarmReaction = alarmReaction;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
