package countingsheep.alarm.db.repositories.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.db.dao.BaseDao;
import countingsheep.alarm.db.entities.DbEntity;

public class GenericTaskList<TDao extends BaseDao, TReturn extends DbEntity> extends AsyncTask<TReturn, Void, List<TReturn>> {


    public interface OnTaskHandler<TDao extends BaseDao, TReturn extends DbEntity> {
        List<TReturn> doInBackground(TDao dao);

        void onPostExecute(List<TReturn> returnedValues);
    }


    private WeakReference<TDao> daoWeakReference;
    private WeakReference<OnTaskHandler<TDao, TReturn>> onTaskHandlerWeakReference;

    public GenericTaskList(TDao dao, OnTaskHandler<TDao, TReturn> onTaskHandler) {
        this.daoWeakReference = new WeakReference<>(dao);
        this.onTaskHandlerWeakReference = new WeakReference<>(onTaskHandler);
    }

    @Override
    protected List<TReturn> doInBackground(TReturn... params) {

        TDao dao = daoWeakReference.get();
        if (dao == null)
            return null;

        OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
        if (onTaskHandler != null) {
            return onTaskHandler.doInBackground(dao);
        }
        else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<TReturn> treturn) {

        OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
        if (onTaskHandler != null) {
            onTaskHandler.onPostExecute(treturn);
        }
    }
}


