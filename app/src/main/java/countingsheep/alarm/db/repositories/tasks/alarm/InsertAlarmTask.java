package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.entities.Alarm;

/**
 * This task is used to perform async the Insert of an alarm operation.
 * It needs to be stored in a separate static class, which uses WekReferences not to have memory leaks
 * Why? https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur
 */
public class InsertAlarmTask extends AsyncTask<Void, Void, Long> {

    private WeakReference<AlarmDatabase> alarmDatabaseWeakReference;
    private WeakReference<OnAsyncResponse<Long>> onAsyncResponseWeakReference;
    private Alarm alarm;

    public InsertAlarmTask(AlarmDatabase alarmDatabase,
                           Alarm alarm)
    {
        this(alarmDatabase, alarm, null);
    }


    public InsertAlarmTask(AlarmDatabase alarmDatabase,
                           Alarm alarm,
                           OnAsyncResponse<Long> onAsyncResponse) {
        this.alarmDatabaseWeakReference = new WeakReference<>(alarmDatabase);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
        this.alarm = alarm;
    }

    @Override
    protected Long doInBackground(Void... voids) {

        AlarmDatabase alarmDatabase = alarmDatabaseWeakReference.get();

        if(alarmDatabase==null)
            return null;
        
        long alarmId = alarmDatabase.alarmDao().insert(alarm);
        return alarmId;
    }

    @Override
    protected void onPostExecute(Long result) {

        OnAsyncResponse<Long> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}
