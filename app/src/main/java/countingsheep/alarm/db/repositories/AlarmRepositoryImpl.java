package countingsheep.alarm.db.repositories;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.entities.Alarm;
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
        new InsertAlarmTask(alarmDatabase, alarm).execute();
    }

    /**
     * This task is used to perform async the Insert of an alarm operation.
     * It needs to be stored in a separate static class, which uses WekReferences not to have memory leaks
     * Why? https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur
     */
    static class InsertAlarmTask extends AsyncTask<Void, Void, Void>{

        private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
        private Alarm alarm;

        public InsertAlarmTask(AlarmDatabase alarmDatabase,
                               Alarm alarm) {
            this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
            this.alarm = alarm;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDatabaseWeakReference.get().alarmDao().insert(alarm);
            return null;
        }
    }

    @Override
    public void update(Alarm alarm) {
        new UpdateAlarmTask(alarmDatabase, alarm).execute();
    }

    static class UpdateAlarmTask extends AsyncTask<Void, Void, Void>{

        private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
        private Alarm alarm;

        public UpdateAlarmTask(AlarmDatabase alarmDatabase,
                               Alarm alarm) {
            this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
            this.alarm = alarm;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDatabaseWeakReference.get().alarmDao().update(alarm);
            return null;
        }
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
