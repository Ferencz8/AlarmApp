package countingsheep.alarm.util;

import androidx.room.TypeConverter;

import countingsheep.alarm.core.domain.enums.Feature;
import countingsheep.alarm.core.domain.enums.FeatureReaction;

public class TypeConverterImpl {
    @TypeConverter
    public static Feature toFeatureType(int type) {
        if (type == Feature.Battle.getCode()) {
            return Feature.Battle;
        } else if (type == Feature.LeaderBoard.getCode()) {
            return Feature.LeaderBoard;
        } else if (type == Feature.RoastAFriend.getCode()) {
            return Feature.RoastAFriend;
        } else if (type == Feature.RoastChat.getCode()) {
            return Feature.RoastChat;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toFeatureInt(Feature type) {
        return type.getCode();
    }

    @TypeConverter
    public static FeatureReaction toFeatureReactionType(int type) {
        if (type == FeatureReaction.Like.getCode()) {
            return FeatureReaction.Like;
        } else if (type == FeatureReaction.DisLike.getCode()) {
            return FeatureReaction.DisLike;
        } else if (type == FeatureReaction.NotReacted.getCode()) {
            return FeatureReaction.NotReacted;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toFeatureReactionInt(FeatureReaction type) {
        return type.getCode();
    }
}
