package countingsheep.alarm.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = Alarm.class,
        parentColumns = "id",
        childColumns = "alarmId",
        onDelete = SET_NULL,
        onUpdate = CASCADE),
        indices = {@Index("alarmId")})
public class AlarmReaction extends DbEntity implements Serializable {

    @SerializedName("deviceAlarmReactionId")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "reactedAt")
    @TypeConverters({TimestampConverter.class})
    @Expose
    private Date reactedAt;

    /**
     * The current minutes of an alarm need to be stored here also, because the alarm's minutes can change
     * and this record(alarm reaction) needs to store the exact point in time values
     */
    @Expose
    private int currentMinutes;


    /**
     * The current hour of an alarm needs to be stored here also, because the alarm's hour can change
     * and this record(alarm reaction) needs to store the exact point in time value
     */
    @Expose
    private int currentHour;

    @SerializedName("deviceAlarmId")
    @Expose
    private int alarmId;

    @Expose
    private boolean isSnooze;

    private boolean isSynced;

    //used to determine whether an alarm reaction requires payment processing to be done
    //For free credit alarms & awake reaction this is false, otherwise true
    private boolean isPayable;

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public int getCurrentMinutes() {
        return currentMinutes;
    }

    public void setCurrentMinutes(int currentMinutes) {
        this.currentMinutes = currentMinutes;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(Date reactedAt) {
        this.reactedAt = reactedAt;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isSnooze() {
        return isSnooze;
    }

    public void setSnooze(boolean snooze) {
        isSnooze = snooze;
    }

    public boolean isPayable() {
        return isPayable;
    }

    public void setPayable(boolean payable) {
        isPayable = payable;
    }
}
