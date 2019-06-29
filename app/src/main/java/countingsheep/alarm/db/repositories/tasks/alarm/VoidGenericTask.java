package countingsheep.alarm.db.repositories.tasks.alarm;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.db.dao.BaseDao;

public class VoidGenericTask <TDao extends BaseDao> extends AsyncTask<Void, Void, Void> {


    public interface OnTaskHandler<TDao extends BaseDao>{
        void doInBackground(TDao dao);

        void onPostExecute();
    }


    private WeakReference<TDao> daoWeakReference;
    private WeakReference<VoidGenericTask.OnTaskHandler<TDao>> onTaskHandlerWeakReference;

    public VoidGenericTask(TDao dao, VoidGenericTask.OnTaskHandler<TDao> onTaskHandler) {
        this.daoWeakReference = new WeakReference<>(dao);
        this.onTaskHandlerWeakReference = new WeakReference<>(onTaskHandler);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        TDao dao = daoWeakReference.get();
        if(dao!=null) {


            VoidGenericTask.OnTaskHandler<TDao> onTaskHandler = this.onTaskHandlerWeakReference.get();
            onTaskHandler.doInBackground(dao);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void treturn) {

        VoidGenericTask.OnTaskHandler<TDao> onTaskHandler = this.onTaskHandlerWeakReference.get();
        if(onTaskHandler!=null) {
            onTaskHandler.onPostExecute();
        }
    }
}
