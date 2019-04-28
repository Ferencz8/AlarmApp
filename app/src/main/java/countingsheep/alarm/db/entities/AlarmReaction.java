package countingsheep.alarm.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = Alarm.class,
        parentColumns = "id",
        childColumns = "alarmId",
        onDelete = SET_NULL,
        onUpdate = CASCADE))
public class AlarmReaction implements Serializable {

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

    @Expose
    private int alarmId;

    @Expose
    private boolean isSnooze;

    private boolean isSynced;

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
}
