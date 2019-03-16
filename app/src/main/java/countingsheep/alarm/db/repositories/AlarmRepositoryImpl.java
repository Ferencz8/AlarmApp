package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import bolts.Task;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.db.repositories.tasks.alarm.InsertAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.UpdateAlarmTask;

@Singleton
public class AlarmRepositoryImpl implements AlarmRepository {

    private AlarmDatabase alarmDatabase;

    @Inject
    public AlarmRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.alarmDatabase = alarmDatabase;
    }


    @Override
    public void insert(final Alarm alarm){
        new InsertAlarmTask(alarmDatabase, alarm).execute();
    }

    @Override
    public void insert(final Alarm alarm, OnAsyncResponse<Long> onAsyncResponse){
        new InsertAlarmTask(alarmDatabase, alarm, onAsyncResponse).execute();
    }

    @Override
    public void update(Alarm alarm) {
        new UpdateAlarmTask(alarmDatabase, alarm).execute();
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
