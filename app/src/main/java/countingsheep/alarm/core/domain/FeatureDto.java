package countingsheep.alarm.core.domain;

import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;

import countingsheep.alarm.core.domain.enums.Feature;
import countingsheep.alarm.core.domain.enums.FeatureReaction;
import countingsheep.alarm.util.TypeConverterImpl;

public class FeatureDto {

    @Expose
    private int userId;

    @TypeConverters({TypeConverterImpl.class})
    @Expose
    private Feature feature;


    @TypeConverters({TypeConverterImpl.class})
    @Expose
    private FeatureReaction featureReaction;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public FeatureReaction getFeatureReaction() {
        return featureReaction;
    }

    public void setFeatureReaction(FeatureReaction featureReaction) {
        this.featureReaction = featureReaction;
    }
}
