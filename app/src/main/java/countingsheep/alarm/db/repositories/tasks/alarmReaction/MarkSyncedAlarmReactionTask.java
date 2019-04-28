package countingsheep.alarm.db.repositories.tasks.alarmReaction;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.db.dao.AlarmReactionDao;

public class MarkSyncedAlarmReactionTask  extends AsyncTask<Void, Void, Void> {

    private WeakReference<AlarmReactionDao> alarmReactionDaoWeakReference;
    private List<Integer> alarmReactionsIds;

    public MarkSyncedAlarmReactionTask(AlarmReactionDao dao,
                                   List<Integer> alarmReactionsIds)
    {
        this.alarmReactionDaoWeakReference = new WeakReference<>(dao);
        this.alarmReactionsIds = alarmReactionsIds;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AlarmReactionDao alarmReactionDao = alarmReactionDaoWeakReference.get();

        if(alarmReactionDao!=null) {
            alarmReactionDao.markAlarmReactionsSynced(alarmReactionsIds);
        }

        return null;
    }
}
