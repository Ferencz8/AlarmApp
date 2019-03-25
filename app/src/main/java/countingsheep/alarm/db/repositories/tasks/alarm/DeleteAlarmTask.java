package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.db.AlarmDatabase;

public class DeleteAlarmTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
    private int alarmId;

    public DeleteAlarmTask(AlarmDatabase alarmDatabase,
                           int alarmId) {
        this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
        this.alarmId = alarmId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AlarmDatabase alarmDatabase = alarmDatabaseWeakReference.get();

        if(alarmDatabase!=null) {
            alarmDatabase.alarmDao().delete(alarmId);
        }

        return null;
    }
}
