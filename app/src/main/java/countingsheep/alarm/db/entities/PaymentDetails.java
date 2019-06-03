package countingsheep.alarm.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;

import java.util.Date;

import countingsheep.alarm.util.PaymentStatusConverter;
import countingsheep.alarm.util.TimestampConverter;

@Entity
public class PaymentDetails extends  DbEntity{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Expose
    private String transactionId;

    @Expose
    @ColumnInfo(name = "dateCreated")
    @TypeConverters({TimestampConverter.class})
    private Date dateCreated;

    @Expose
    @ColumnInfo(name = "dateModified")
    @TypeConverters({TimestampConverter.class})
    private Date dateModified;

    private int alarmReactionId;

    private int amount;

    @TypeConverters({PaymentStatusConverter.class})
    private PaymentStatus paymentStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getAlarmReactionId() {
        return alarmReactionId;
    }

    public void setAlarmReactionId(int alarmReactionId) {
        this.alarmReactionId = alarmReactionId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
