package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.dao.AlarmDao;
import countingsheep.alarm.db.entities.Alarm;

public class GetAlarmTask extends AsyncTask<Integer, Void, Alarm> {

    private WeakReference<AlarmDao> alarmDaoWeakReference;
    private WeakReference<OnAsyncResponse<Alarm>> onAsyncResponseWeakReference;

    public GetAlarmTask(AlarmDao alarmDao, OnAsyncResponse<Alarm> onAsyncResponse) {
        this.alarmDaoWeakReference = new WeakReference<>(alarmDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
    }

    @Override
    protected Alarm doInBackground(Integer... ids ) {

        AlarmDao alarmDao = alarmDaoWeakReference.get();
        if (alarmDao == null || ids.length == 0)
            return null;

        return alarmDao.getById(ids[0]);
    }

    @Override
    protected void onPostExecute(Alarm alarm) {

        OnAsyncResponse onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if (onAsyncResponse != null) {
            onAsyncResponse.processResponse(alarm);
        }
    }
}