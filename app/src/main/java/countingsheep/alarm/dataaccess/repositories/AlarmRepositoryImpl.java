package countingsheep.alarm.dataaccess.repositories;

import android.os.AsyncTask;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.dataaccess.AlarmDatabase;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.core.contracts.data.AlarmRepository;

@Singleton
public class AlarmRepositoryImpl implements AlarmRepository {

    private AlarmDatabase alarmDatabase;

    @Inject
    public AlarmRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.alarmDatabase = alarmDatabase;
    }


    @Override
    public void insert(final Alarm alarm){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                alarmDatabase.alarmDao().insert(alarm);
                return null;
            }
        }.execute();
    }

    @Override
    public void update(Alarm alarm) {
        alarmDatabase.alarmDao().update(alarm);
    }


    @Override
    public Alarm get(int id) {
        return alarmDatabase.alarmDao().getById(id);
    }


    @Override
    public List<Alarm> get() {

        return alarmDatabase.alarmDao().getAll();
    }

    @Override
    public List<Alarm> getAllUnsyced() {
        return alarmDatabase.alarmDao().getUnSynced();
    }

    @Override
    public void markAlarmsSynced(List<Integer> alarmIds) {
        alarmDatabase.alarmDao().markAlarmsSynced(alarmIds);
    }
}
