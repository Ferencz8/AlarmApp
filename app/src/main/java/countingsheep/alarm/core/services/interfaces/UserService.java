package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.OnResult;

public interface UserService {

    void sendRoastFeedback(int messageId, String content, OnResult onResult);

    void sendGeneralFeedback(String content, OnResult onResult);

    void syncCredits();
}
