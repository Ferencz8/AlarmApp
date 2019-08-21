package countingsheep.alarm.db.repositories.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.domain.Pair;
import countingsheep.alarm.db.dao.AlarmDao;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.repositories.tasks.alarm.GetAlarmTask;

public class PairTask extends AsyncTask<Integer, Void, Pair<Alarm, AlarmReaction>> {

    private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
    private WeakReference<OnAsyncResponse<Pair<Alarm, AlarmReaction>>> onAsyncResponseWeakReference;

    public PairTask(AlarmReactionDao alarmReactionDao, OnAsyncResponse<Pair<Alarm, AlarmReaction>> onAsyncResponse) {
        this.alarmReactionDaoWeakReference = new WeakReference<>(alarmReactionDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
    }

    @Override
    protected Pair<Alarm, AlarmReaction> doInBackground(Integer... ids ) {

        Log.d(GetAlarmTask.class.getSimpleName(), "Entering doInBackground");
        AlarmReactionDao alarmReactionDao = alarmReactionDaoWeakReference.get();
        if (alarmReactionDao == null || ids.length == 0)
            return null;

        AlarmReaction alarmReaction = alarmReactionDao.getById(ids[0]);
        Alarm alarm = alarmReactionDao.getAlarm(ids[0]);
        return new Pair<>(alarm, alarmReaction);
    }

    @Override
    protected void onPostExecute(Pair<Alarm, AlarmReaction> pair) {
        Log.d(GetAlarmTask.class.getSimpleName(), "Entering onPostExecute");
        OnAsyncResponse onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if (onAsyncResponse != null) {
            onAsyncResponse.processResponse(pair);
        }

        Log.d(GetAlarmTask.class.getSimpleName(), "Finished onPostExecute");
    }
}