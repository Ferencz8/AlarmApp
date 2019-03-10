package countingsheep.alarm.db.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.MessageRepository;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.MessageDao;
import countingsheep.alarm.db.entities.Message;

@Singleton
public class MessageRepositoryImpl implements MessageRepository {

    private AlarmDatabase alarmDatabase;
    private MessageDao dao;

    @Inject
    public MessageRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.alarmDatabase = alarmDatabase;
        this.dao = alarmDatabase.messageDao();
    }

    @Override
    public void insert(Message message) {
        dao.insert(message);
    }

    @Override
    public void update(Message message) {
        dao.update(message);
    }

    @Override
    public Message get(int id) {
        return dao.getById(id);
    }

    @Override
    public List<Message> get() {
        return dao.getAll();
    }

    @Override
    public Message getNotSeen() {
        return dao.getNotSeen();
    }

    @Override
    public List<Message> getAllUnsynced(boolean seenValue) {
        return dao.getAllUnsynced(seenValue);
    }

    @Override
    public void markMessagesSynced(List<Integer> messageIds) {
        dao.markMessagesSynced(messageIds);
    }
}
