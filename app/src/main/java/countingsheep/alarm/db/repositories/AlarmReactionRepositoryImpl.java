package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.entities.AlarmHistoryEmbedded;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.repositories.tasks.alarm.GetAllGenericTask;
import countingsheep.alarm.db.repositories.tasks.alarmReaction.GetCountAlarmsTask;
import countingsheep.alarm.db.repositories.tasks.alarmReaction.GetSnoozeRateTask;
import countingsheep.alarm.db.repositories.tasks.alarmReaction.InsertAlarmReactionTask;
import countingsheep.alarm.db.repositories.tasks.alarmReaction.MarkSyncedAlarmReactionTask;


@Singleton
public class AlarmReactionRepositoryImpl implements AlarmReactionRepository {

    private AlarmReactionDao dao;

    @Inject
    public AlarmReactionRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.dao = alarmDatabase.alarmReactionDao();
    }


    @Override
    public void insert(final AlarmReaction alarmReaction) {
        new InsertAlarmReactionTask(dao, alarmReaction).execute();
    }

    @Override
    public void insert(AlarmReaction alarmReaction, OnAsyncResponse<Long> onAsyncResponse) {
        new InsertAlarmReactionTask(dao, alarmReaction, onAsyncResponse).execute();
    }


    @Override
    public void update(AlarmReaction alarmReaction) {
        new UpdateAlarmReactionTask(dao, alarmReaction).execute();
    }

    static class UpdateAlarmReactionTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
        private AlarmReaction alarmReaction;

        public UpdateAlarmReactionTask(AlarmReactionDao alarmReactionDao,
                                       AlarmReaction alarmReaction) {
            this.alarmReactionDaoWeakReference = new WeakReference<>(alarmReactionDao);
            this.alarmReaction = alarmReaction;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmReactionDaoWeakReference.get().update(alarmReaction);
            return null;
        }
    }


    @Override
    public AlarmReaction get(int id) {
        return dao.getById(id);
    }


    @Override
    public List<AlarmReaction> get() {

        return dao.getAll();
    }

    public List<AlarmReaction> getAllUnsynced() {

        return dao.getAllUnSynced();
    }

    @Override
    public void markAlarmsSynced(List<Integer> alarmReactionIds) {
        new MarkSyncedAlarmReactionTask(dao, alarmReactionIds).execute();
    }

    @Override
    public void getCountAlarms(OnAsyncResponse<Integer> response) {
        new GetCountAlarmsTask(dao, response).execute();
    }

    @Override
    public void getSnoozeRate(OnAsyncResponse<Integer> response) {
        new GetSnoozeRateTask(dao, response).execute();
    }

    @Override
    public void getAllAlarms(OnAsyncResponse<List<AlarmHistoryEmbedded>> reactionOnAsyncResponse) {
        new GetAllGenericTask<AlarmReactionDao, AlarmHistoryEmbedded>(dao, new GetAllGenericTask.OnTaskHandler<AlarmReactionDao, AlarmHistoryEmbedded>() {
            @Override
            public List<AlarmHistoryEmbedded> doInBackground(AlarmReactionDao o) {
               return o.getAllAlarmHistory();
            }

            @Override
            public void onPostExecute(List<AlarmHistoryEmbedded> returnedValues) {
                reactionOnAsyncResponse.processResponse(returnedValues);
            }
        }).execute();
    }
}
