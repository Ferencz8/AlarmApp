package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.entities.Alarm;

public class UpdateAlarmTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
    private Alarm alarm;

    public UpdateAlarmTask(AlarmDatabase alarmDatabase,
                           Alarm alarm) {
        this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
        this.alarm = alarm;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AlarmDatabase alarmDatabase = alarmDatabaseWeakReference.get();

        if(alarmDatabase!=null) {
            alarmDatabase.alarmDao().update(alarm);
        }

        return null;
    }
}
