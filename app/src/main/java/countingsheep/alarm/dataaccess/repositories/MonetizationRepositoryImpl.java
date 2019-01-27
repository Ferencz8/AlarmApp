package countingsheep.alarm.dataaccess.repositories;


import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.MonetizationRepository;
import countingsheep.alarm.dataaccess.AlarmDatabase;
import countingsheep.alarm.dataaccess.dao.MonetizationDao;
import countingsheep.alarm.dataaccess.entities.Monetization;

@Singleton
public class MonetizationRepositoryImpl implements MonetizationRepository {

    private AlarmDatabase alarmDatabase;
    private MonetizationDao dao;

    @Inject
    public MonetizationRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.alarmDatabase = alarmDatabase;
        this.dao = alarmDatabase.monetizationDao();
    }


    @Override
    public void insert(final Monetization monetization){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                dao.insert(monetization);
                return null;
            }
        }.execute();
    }

    @Override
    public void update(Monetization monetization) {
        dao.update(monetization);
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