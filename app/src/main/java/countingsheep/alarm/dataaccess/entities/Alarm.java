package countingsheep.alarm.dataaccess.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

@Entity
public class Alarm implements Serializable {

    @PrimaryKey(autoGenerate = true)
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

    private boolean isSynced;

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
