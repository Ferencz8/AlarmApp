package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.domain.enums.Feature;
import countingsheep.alarm.core.domain.enums.FeatureReaction;

public interface UserService {

    void sendRoastFeedback(int messageId, String content, OnResult onResult);

    void sendGeneralFeedback(String content, OnResult onResult);

    void syncCredits();

    void addFeatureReaction(Feature feature, FeatureReaction featureReaction, OnResult onResult);
}
