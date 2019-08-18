package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.Message;

public interface MessageRepository {

    void insert(Message message);

    void insert(Message message, OnAsyncResponse<Long> onAsyncResponse);

    void update(Message message);

    Message get(int id);

    List<Message> get();

    void getAllHistory(OnAsyncResponse<List<Message>> onAsyncResponse);

    Message getNotSeen();

    List<Message> getAllUnsynced(boolean seenValue);

    void markMessagesSynced(List<Integer> messageIds);
}
