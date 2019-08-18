package countingsheep.alarm.db.repositories.tasks;

import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import java.lang.ref.WeakReference;

import countingsheep.alarm.db.dao.BaseDao;

public class GenericTaskObject<TDao extends BaseDao, TReturn> extends AsyncTask<Void, Void, TReturn> {


    public interface OnTaskHandler<TDao extends BaseDao, TReturn> {
        TReturn doInBackground(TDao dao);

        void onPostExecute(TReturn returnedValues);
    }


    private WeakReference<TDao> daoWeakReference;
    private WeakReference<OnTaskHandler<TDao, TReturn>> onTaskHandlerWeakReference;

    public GenericTaskObject(TDao dao, OnTaskHandler<TDao, TReturn> onTaskHandler) {
        this.daoWeakReference = new WeakReference<>(dao);
        this.onTaskHandlerWeakReference = new WeakReference<>(onTaskHandler);
    }

    @Override
    protected TReturn doInBackground(Void... voids) {

        try {
            TDao dao = daoWeakReference.get();
            if (dao == null)
                return null;

            OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
            return onTaskHandler != null ? onTaskHandler.doInBackground(dao) : null;
        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(TReturn treturn) {

        OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
        if (onTaskHandler != null) {
            onTaskHandler.onPostExecute(treturn);
        }
    }
}
