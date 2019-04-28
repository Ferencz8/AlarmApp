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
import countingsheep.alarm.db.repositories.tasks.alarm.DeleteAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.GetAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.GetAllAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.GetAllTurnedOnAlarmsTask;
import countingsheep.alarm.db.repositories.tasks.alarm.InsertAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.UpdateAlarmTask;
import countingsheep.alarm.db.repositories.tasks.alarm.UpdateSyncedAlarmTask;

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
    public void delete(int alarmId) {
        new DeleteAlarmTask(alarmDatabase, alarmId).execute();
    }


    @Override
    public Alarm get(int id) {
        return alarmDatabase.alarmDao().getById(id);
    }

    @Override
    public void get(int id, OnAsyncResponse<Alarm> onAsyncResponse) {
        new GetAlarmTask(alarmDatabase.alarmDao(), onAsyncResponse).execute(id);
    }


    @Override
    public List<Alarm> get() {

        return alarmDatabase.alarmDao().getAll();
    }

    @Override
    public void get(OnAsyncResponse<List<Alarm>> onAsyncResponse) {

        new GetAllAlarmTask(alarmDatabase.alarmDao(), onAsyncResponse).execute();
    }


    @Override
    public List<Alarm> getAllUnsyced() {
        return alarmDatabase.alarmDao().getUnSynced();
    }

    @Override
    public List<Alarm> getOnOrOffAlarms(boolean state) {
        return alarmDatabase.alarmDao().getOnOrOffAlarms();
    }

    @Override
    public void getOnOrOffAlarms(OnAsyncResponse<List<Alarm>> onAsyncResponse, boolean state) {
        //TODO:: state is not used
        new GetAllTurnedOnAlarmsTask(alarmDatabase.alarmDao(), onAsyncResponse).execute();
    }

    @Override
    public void markAlarmsSynced(List<Integer> alarmIds) {
        new UpdateSyncedAlarmTask(alarmDatabase, alarmIds).execute();
    }
}
