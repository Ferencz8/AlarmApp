package countingsheep.alarm.dataaccess;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import countingsheep.alarm.dataaccess.dao.AlarmDao;
import countingsheep.alarm.dataaccess.dao.AlarmReactionDao;
import countingsheep.alarm.dataaccess.dao.MessageDao;
import countingsheep.alarm.dataaccess.dao.MonetizationDao;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.dataaccess.entities.AlarmReaction;
import countingsheep.alarm.dataaccess.entities.Message;
import countingsheep.alarm.dataaccess.entities.Monetization;

@Database(entities = {
        Alarm.class,
        Message.class,
        AlarmReaction.class,
        Monetization.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    public abstract AlarmReactionDao alarmReactionDao();

    public abstract MessageDao messageDao();

    public abstract MonetizationDao monetizationDao();
}
