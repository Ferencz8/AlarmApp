package countingsheep.alarm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import countingsheep.alarm.db.dao.AlarmDao;
import countingsheep.alarm.db.dao.AlarmReactionDao;
import countingsheep.alarm.db.dao.MessageDao;
import countingsheep.alarm.db.dao.MonetizationDao;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.db.entities.Monetization;


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
