package countingsheep.alarm.util;

import androidx.room.TypeConverter;

import countingsheep.alarm.core.domain.enums.CommentType;

public class CommentTypeConverter {

    @TypeConverter
    public static CommentType toType(int type) {
        if (type == CommentType.RoastResponse.getCode()) {
            return CommentType.RoastResponse;
        } else if (type == CommentType.Feedback.getCode()) {
            return CommentType.Feedback;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toInt(CommentType type) {
        return type.getCode();
    }
}
