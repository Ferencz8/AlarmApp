package countingsheep.alarm.util;

import androidx.room.TypeConverter;

import countingsheep.alarm.db.entities.PaymentStatus;

import static countingsheep.alarm.db.entities.PaymentStatus.Failed;
import static countingsheep.alarm.db.entities.PaymentStatus.Requested;
import static countingsheep.alarm.db.entities.PaymentStatus.Settled;

public class PaymentStatusConverter {

    @TypeConverter
    public static PaymentStatus toStatus(int status) {
        if (status == Requested.getCode()) {
            return Requested;
        } else if (status == Settled.getCode()) {
            return Settled;
        } else if (status == Failed.getCode()) {
            return Failed;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toInt(PaymentStatus status) {
        return status.getCode();
    }
}
