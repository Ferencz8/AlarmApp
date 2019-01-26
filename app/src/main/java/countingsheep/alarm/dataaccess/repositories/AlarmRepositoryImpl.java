package countingsheep.alarm.dataaccess.repositories;

import android.os.AsyncTask;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.dataaccess.AlarmDatabase;
import countingsheep.alarm.dataaccess.dao.AlarmDao;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.core.datainterfaces.AlarmRepository;
import countingsheep.alarm.dataaccess.mappers.AlarmMapper;

@Singleton
public class AlarmRepositoryImpl implements AlarmRepository {

    private AlarmDatabase alarmDatabase;
    private AlarmMapper alarmMapper;

    @Inject
    public AlarmRepositoryImpl(AlarmDatabase alarmDatabase, AlarmMapper alarmMapper) {
        this.alarmDatabase = alarmDatabase;
        this.alarmMapper = alarmMapper;
    }


    @Override
    public void insert(final AlarmModel alarm){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Alarm alarmDb = alarmMapper.mapToAlarmDb(alarm);
                alarmDatabase.alarmDao().insert(alarmDb);
                return null;
            }
        }.execute();
    }

    @Override
    public void update(AlarmModel alarm) {
        alarmDatabase.alarmDao().update(alarmMapper.mapToAlarmDb(alarm));
    }


    @Override
    public AlarmModel get(int id) {
        return alarmMapper.mapToAlarmModel(alarmDatabase.alarmDao().getById(id));
    }


    @Override
    public List<AlarmModel> get() {

        return alarmMapper.mapToAlarmModel(alarmDatabase.alarmDao().getAll());
    }
}
