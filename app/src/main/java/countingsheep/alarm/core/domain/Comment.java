package countingsheep.alarm.core.domain;

import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;

import java.util.Date;

import countingsheep.alarm.core.domain.enums.CommentType;
import countingsheep.alarm.util.CommentTypeConverter;

public class Comment {

    @Expose
    private int userId;

    @Expose
    private String content;

    @Expose
    private Date dateCreated;

    @TypeConverters({CommentTypeConverter.class})
    @Expose
    private CommentType type;

    @Expose
    private String value;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
