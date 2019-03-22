package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.entities.AlarmReaction;


@Singleton
public class AlarmReactionRepositoryImpl implements AlarmReactionRepository {

    private AlarmReactionDao dao;

    @Inject
    public AlarmReactionRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.dao = alarmDatabase.alarmReactionDao();
    }


    @Override
    public void insert(final AlarmReaction alarmReaction){

        new InsertAlarmReactionTask(dao, alarmReaction).execute();
    }

    static class InsertAlarmReactionTask extends AsyncTask<Void, Void, Void>{

        private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
        private AlarmReaction alarmReaction;

        public InsertAlarmReactionTask(AlarmReactionDao alarmReactionDao,
                                       AlarmReaction alarmReaction) {
            this.alarmReactionDaoWeakReference = new WeakReference<>(alarmReactionDao);
            this.alarmReaction = alarmReaction;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmReactionDaoWeakReference.get().insert(alarmReaction);
            return null;
        }
    }

    @Override
    public void update(AlarmReaction alarmReaction) {
        new UpdateAlarmReactionTask(dao, alarmReaction).execute();
    }

    static class UpdateAlarmReactionTask extends AsyncTask<Void, Void, Void>{

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
}
