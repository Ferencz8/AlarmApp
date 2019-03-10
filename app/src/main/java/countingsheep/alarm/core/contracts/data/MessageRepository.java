package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.Message;

public interface MessageRepository {

    void insert(Message message);

    void update(Message message);

    Message get(int id);

    List<Message> get();

    Message getNotSeen();

    List<Message> getAllUnsynced(boolean seenValue);

    void markMessagesSynced(List<Integer> messageIds);
}
