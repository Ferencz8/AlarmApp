package countingsheep.alarm.db.repositories.tasks.alarmReaction;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.dao.AlarmReactionDao;

public class GetSnoozeRateTask extends AsyncTask<Void, Void, Integer> {
    private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
    private WeakReference<OnAsyncResponse<Integer>> onAsyncResponseWeakReference;

    public GetSnoozeRateTask(AlarmReactionDao paymentDetailsDao)
    {
        this(paymentDetailsDao,  null);
    }


    public GetSnoozeRateTask(AlarmReactionDao alarmReactionDao,
                              OnAsyncResponse<Integer> onAsyncResponse) {
        this.alarmReactionDaoWeakReference = new WeakReference<>(alarmReactionDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
    }
    @Override
    protected Integer doInBackground(Void... voids) {
        AlarmReactionDao alarmReactionDao = alarmReactionDaoWeakReference.get();

        if(alarmReactionDao==null)
            return null;


        Integer snoozeCount = alarmReactionDao.getAllAlarmsCount(true);
        Integer alarmsCount = alarmReactionDao.getAllAlarmsCount();

        if(alarmsCount == 0){
            return 0;
        } else {
            Integer snoozeRate = (int) snoozeCount / alarmsCount;
            return snoozeRate;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        OnAsyncResponse<Integer> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}
