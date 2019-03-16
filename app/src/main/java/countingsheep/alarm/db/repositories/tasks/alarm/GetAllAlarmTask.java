package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.dao.AlarmDao;
import countingsheep.alarm.db.entities.Alarm;

public class GetAllAlarmTask extends AsyncTask<Void, Void, List<Alarm>> {

    private WeakReference<AlarmDao> alarmDaoWeakReference;
    private WeakReference<OnAsyncResponse<List<Alarm>>> onAsyncResponseWeakReference;

    public GetAllAlarmTask(AlarmDao alarmDao, OnAsyncResponse<List<Alarm>> onAsyncResponse) {
        this.alarmDaoWeakReference = new WeakReference<>(alarmDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
    }

    @Override
    protected List<Alarm> doInBackground(Void... voids) {

        AlarmDao alarmDao = alarmDaoWeakReference.get();
        if(alarmDao==null)
            return null;

        return    alarmDao.getAll();
    }

    @Override
    protected void onPostExecute(List<Alarm> alarms) {

        OnAsyncResponse onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(alarms);
        }
    }
}