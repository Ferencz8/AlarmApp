package countingsheep.alarm.db.repositories;


import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.MonetizationRepository;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.MonetizationDao;
import countingsheep.alarm.db.entities.Monetization;

@Singleton
public class MonetizationRepositoryImpl implements MonetizationRepository {

    private MonetizationDao dao;

    @Inject
    public MonetizationRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.dao = alarmDatabase.monetizationDao();
    }


    @Override
    public void insert(final Monetization monetization){
        new InsertMonetizationTask(this.dao, monetization).execute();
    }

    static class InsertMonetizationTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MonetizationDao> monetizationDaoWeakReference;
        private Monetization monetization;

        public InsertMonetizationTask(MonetizationDao monetizationDao,
                                 Monetization monetization) {
            this.monetizationDaoWeakReference = new WeakReference<>(monetizationDao);
            this.monetization = monetization;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            monetizationDaoWeakReference.get().insert(monetization);
            return null;
        }
    }

    @Override
    public void update(Monetization monetization) {
        new UpdateMonetizationTask(this.dao, monetization).execute();
    }

    static class UpdateMonetizationTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MonetizationDao> monetizationDaoWeakReference;
        private Monetization monetization;

        public UpdateMonetizationTask(MonetizationDao monetizationDao,
                                      Monetization monetization) {
            this.monetizationDaoWeakReference = new WeakReference<>(monetizationDao);
            this.monetization = monetization;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            monetizationDaoWeakReference.get().update(monetization);
            return null;
        }
    }


    @Override
    public Monetization get(int id) {
        return dao.getById(id);
    }


    @Override
    public List<Monetization> get() {

        return dao.getAll();
    }
}