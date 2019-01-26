package countingsheep.alarm.dataaccess;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import countingsheep.alarm.dataaccess.dao.AlarmDao;
import countingsheep.alarm.dataaccess.entities.Alarm;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();
}
