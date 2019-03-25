package countingsheep.alarm.db.repositories.tasks.alarmReaction;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.entities.AlarmReaction;

public class InsertAlarmReactionTask extends AsyncTask<Void, Void, Long> {

    private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
    private WeakReference<OnAsyncResponse<Long>> onAsyncResponseWeakReference;
    private AlarmReaction alarmReaction;

    public InsertAlarmReactionTask(AlarmReactionDao dao,
                                   AlarmReaction alarmReaction)
    {
        this(dao, alarmReaction, null);
    }


    public InsertAlarmReactionTask(AlarmReactionDao dao,
                           AlarmReaction alarmReaction,
                           OnAsyncResponse<Long> onAsyncResponse) {
        this.alarmReactionDaoWeakReference = new WeakReference<>(dao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
        this.alarmReaction = alarmReaction;
    }

    @Override
    protected Long doInBackground(Void... voids) {

        AlarmReactionDao alarmReactionDao = alarmReactionDaoWeakReference.get();

        if(alarmReactionDao==null)
            return null;

        long alarmReactionId = alarmReactionDao.insert(alarmReaction);
        return alarmReactionId;
    }

    @Override
    protected void onPostExecute(Long result) {

        OnAsyncResponse<Long> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}
