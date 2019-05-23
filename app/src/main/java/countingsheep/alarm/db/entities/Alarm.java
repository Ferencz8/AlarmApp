package countingsheep.alarm.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

@Entity
public class Alarm implements Serializable {

    public Alarm() {
        this.setVobrateOn(true);
        this.setVolume(100);
    }

    @PrimaryKey(autoGenerate = true)
    @SerializedName("deviceId")
    @Expose
    private int id;

    @Expose
    @ColumnInfo(name = "dateCreated")
    @TypeConverters({TimestampConverter.class})
    private Date dateCreated;

    @Expose
    @ColumnInfo(name = "dateModified")
    @TypeConverters({TimestampConverter.class})
    private Date dateModified;

    @Expose
    private int minutes;

    @Expose
    private int hour;

    private boolean isTurnedOn;

    private String title;

    private boolean isVobrateOn;

    private int volume;

    private String repeatDays;

    private int snoozeAmount;

    private boolean isSynced;

    private String ringtoneName;

    private String ringtonePath;

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public String getRingtonePath() {
        return ringtonePath;
    }

    public void setRingtonePath(String ringtonePath) {
        this.ringtonePath = ringtonePath;
    }

    public int getSnoozeAmount() {
        return snoozeAmount;
    }

    public void setSnoozeAmount(int snoozeAmount) {
        this.snoozeAmount = snoozeAmount;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isTurnedOn() {
        return isTurnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        isTurnedOn = turnedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVobrateOn() {
        return isVobrateOn;
    }

    public void setVobrateOn(boolean vobrateOn) {
        isVobrateOn = vobrateOn;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
