package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.entities.AlarmReaction;


@Singleton
public class AlarmReactionRepositoryImpl implements AlarmReactionRepository {

    private AlarmDatabase alarmDatabase;
    private AlarmReactionDao dao;

    @Inject
    public AlarmReactionRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.alarmDatabase = alarmDatabase;
        this.dao = this.alarmDatabase.alarmReactionDao();
    }


    @Override
    public void insert(final AlarmReaction alarmReaction){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dao.insert(alarmReaction);
                return null;
            }
        }.execute();
    }

    @Override
    public void update(AlarmReaction alarmReaction) {
        dao.update(alarmReaction);
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
