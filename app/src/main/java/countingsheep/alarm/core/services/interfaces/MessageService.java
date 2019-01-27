package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.dataaccess.entities.Message;

public interface MessageService {

    void markSeen(int messageId);

    Message getUnseenMessage();

    void markMessageLiked(int messageId);

    void markMessageShared(int messageId);
}
