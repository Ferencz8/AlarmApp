package countingsheep.alarm.db.repositories.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.db.dao.BaseDao;

public class GetGenericTask2<TDao extends BaseDao, TReturn> extends AsyncTask<Void, Void, TReturn> {


    public interface OnTaskHandler<TDao extends BaseDao, TReturn>{
        TReturn doInBackground(TDao dao);

        void onPostExecute(TReturn returnedValues);
    }


    private WeakReference<TDao> daoWeakReference;
    private WeakReference<OnTaskHandler<TDao, TReturn>> onTaskHandlerWeakReference;

    public GetGenericTask2(TDao dao, OnTaskHandler<TDao, TReturn> onTaskHandler) {
        this.daoWeakReference = new WeakReference<>(dao);
        this.onTaskHandlerWeakReference = new WeakReference<>(onTaskHandler);
    }

    @Override
    protected TReturn doInBackground(Void... voids) {

        TDao dao = daoWeakReference.get();
        if(dao==null)
            return null;

        OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
        return onTaskHandler.doInBackground(dao);
    }

    @Override
    protected void onPostExecute(TReturn treturn) {

        OnTaskHandler<TDao, TReturn> onTaskHandler = this.onTaskHandlerWeakReference.get();
        if(onTaskHandler!=null) {
            onTaskHandler.onPostExecute(treturn);
        }
    }
}
