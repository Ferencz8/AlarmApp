package countingsheep.alarm.dataaccess.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

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
    private Date reactedAt;

    private int alarmId;

    private boolean isSnooze;

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
