package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.db.AlarmDatabase;

public class UpdateSyncedAlarmTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
    private List<Integer> alarmIds;

    public UpdateSyncedAlarmTask(AlarmDatabase alarmDatabase,
                           List<Integer> alarmIds) {
        this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
        this.alarmIds = alarmIds;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AlarmDatabase alarmDatabase = alarmDatabaseWeakReference.get();

        if(alarmDatabase!=null) {
            alarmDatabase.alarmDao().markAlarmsSynced(alarmIds);
        }

        return null;
    }
}
