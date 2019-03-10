package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.db.entities.Message;

public interface MessageService {

    void markSeen(int messageId);

    Message getUnseenMessage();

    void markMessageLiked(int messageId);

    void markMessageShared(int messageId);

    List<Message> getAllUnsynced();

    boolean markSyncedRange(List<Message> unsyncedMessages);
}
