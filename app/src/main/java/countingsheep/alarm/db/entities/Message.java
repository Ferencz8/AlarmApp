package countingsheep.alarm.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

@Entity
public class Message extends DbEntity implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @Expose
    private int id;

    @Expose
    private String content;

    @Expose
    private boolean isHate;

    @Expose
    private boolean isLiked;

    @Expose
    private boolean isShared;

    private boolean isSeen;

    @ColumnInfo(name = "seenAt")
    @TypeConverters({TimestampConverter.class})
    private Date seenAt;

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
