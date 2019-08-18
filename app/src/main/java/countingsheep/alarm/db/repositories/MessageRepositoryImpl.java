package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.MessageRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.MessageDao;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.db.repositories.tasks.GenericTaskObject;
import countingsheep.alarm.db.repositories.tasks.GenericTaskList;

@Singleton
public class MessageRepositoryImpl implements MessageRepository {

    private MessageDao dao;

    @Inject
    public MessageRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.dao = alarmDatabase.messageDao();
    }

    @Override
    public void insert(Message message) {
        new InsertMessageTask(dao, message).execute();
    }

    @Override
    public void insert(Message message, OnAsyncResponse<Long> onAsyncResponse) {
        new GenericTaskObject<MessageDao, Long>(dao, new GenericTaskObject.OnTaskHandler<MessageDao, Long>() {
            @Override
            public Long doInBackground(MessageDao dao) {
                return dao.insert(message);
            }

            @Override
            public void onPostExecute(Long returnedValues) {
                onAsyncResponse.processResponse(returnedValues);
            }
        }).execute();
    }

    static class InsertMessageTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MessageDao> messageDaoWeakReference;
        private Message message;

        public InsertMessageTask(MessageDao messageDao,
                               Message message) {
            this.messageDaoWeakReference = new WeakReference<>(messageDao);
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            messageDaoWeakReference.get().insert(message);
            return null;
        }
    }

    @Override
    public void update(Message message) {
        new UpdateMessageTask(dao, message).execute();
    }

    static class UpdateMessageTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MessageDao> messageDaoWeakReference;
        private Message message;

        public UpdateMessageTask(MessageDao messageDao,
                                 Message message) {
            this.messageDaoWeakReference = new WeakReference<>(messageDao);
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            messageDaoWeakReference.get().update(message);
            return null;
        }
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
    public void getAllHistory(OnAsyncResponse<List<Message>> onAsyncResponse) {
        new GenericTaskList<MessageDao, Message>(dao, new GenericTaskList.OnTaskHandler<MessageDao, Message>() {
            @Override
            public List<Message> doInBackground(MessageDao o) {
                return o.getAllHistory();
            }

            @Override
            public void onPostExecute(List<Message> returnedValues) {
                onAsyncResponse.processResponse(returnedValues);
            }
        }).execute();
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
