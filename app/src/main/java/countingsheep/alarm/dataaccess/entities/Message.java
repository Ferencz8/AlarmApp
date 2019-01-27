package countingsheep.alarm.dataaccess.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

@Entity
public class Message implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;

    private String content;

    private boolean isHate;

    private boolean isLiked;

    private boolean isShared;

    private boolean isSeen;

    @ColumnInfo(name = "seenAt")
    @TypeConverters({TimestampConverter.class})
    private Date seenAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHate() {
        return isHate;
    }

    public void setHate(boolean hate) {
        isHate = hate;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public Date getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(Date seenAt) {
        this.seenAt = seenAt;
    }
}
