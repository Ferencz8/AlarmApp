package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Message;

public interface MessageRepository {

    void insert(Message message);

    void update(Message message);

    Message get(int id);

    List<Message> get();

    Message getNotSeen();
}
