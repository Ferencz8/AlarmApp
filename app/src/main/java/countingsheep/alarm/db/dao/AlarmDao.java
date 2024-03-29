package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.Alarm;

@Dao
public interface AlarmDao extends BaseDao<Alarm>{

    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarm WHERE id =:alarmId")
    Alarm getById(int alarmId);

    @Query("SELECT Alarm.* FROM Alarm\n" +
            "INNER JOIN AlarmReaction ON AlarmReaction.alarmId = Alarm.id\n" +
            "WHERE AlarmReaction.id =:alarmReactionId")
    Alarm getByAlarmReactionId(int alarmReactionId);

    @Query("SELECT * FROM Alarm WHERE isSynced =0")
    List<Alarm> getUnSynced();

    @Query("SELECT * FROM Alarm WHERE isTurnedOn =1 AND IsDeleted =0")
    List<Alarm> getOnOrOffAlarms();

    @Query("UPDATE Alarm SET IsSynced = 1, DateModified = DATETIME('now') WHERE id IN (:alarmIds)")
    void markAlarmsSynced(List<Integer> alarmIds);

    @Query("UPDATE Alarm SET IsDeleted = 1, DateModified = DATETIME('now') WHERE id =:alarmId")
    void delete(int alarmId);

    @Query("SELECT * FROM Alarm WHERE isDeleted =0")
    List<Alarm> getAllNotDeleted();

    @Query("SELECT " +
            "COUNT(*)" +
            "FROM Alarm " +
            "INNER JOIN AlarmReaction ON Alarm.id = AlarmReaction.alarmId " +
            "WHERE AlarmReaction.isSnooze = 1 AND Alarm.id=:alarmId")
    int getSnoozesCount(int alarmId);
//
//    @Query("SELECT * FROM Alarm \n" +
//            "WHERE Alarm.hour >=:minHour AND Alarm.hour <=:maxHour AND Alarm.minutes <=:maxMinutes AND Alarm.minutes >=:minMinutes AND Alarm.isTurnedOn = 1 AND Alarm.isDeleted = 0")
//    List<Alarm> getAlarm(int minHour,int maxHour, int minMinutes, int maxMinutes);

    @Query("\n" +
            "SELECT * FROM Alarm \n" +
            "WHERE Alarm.hour =:minHour AND Alarm.isTurnedOn = 1 AND Alarm.isDeleted = 0\n" +
            "AND Alarm.minutes >=:minMinutes " +
            "UNION\n" +
            "SELECT * FROM Alarm \n" +
            "WHERE Alarm.hour =:maxHour AND Alarm.isTurnedOn = 1 AND Alarm.isDeleted = 0\n" +
            "AND Alarm.minutes <=:maxMinutes ")
    List<Alarm> getAlarm(int minHour,int maxHour, int minMinutes, int maxMinutes);
}
