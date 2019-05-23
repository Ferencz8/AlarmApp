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

    @Query("SELECT * FROM Alarm WHERE isSynced =0")
    List<Alarm> getUnSynced();

    @Query("SELECT * FROM Alarm WHERE isTurnedOn =1")
    List<Alarm> getOnOrOffAlarms();

    @Query("UPDATE Alarm SET IsSynced = 1, DateModified = DATETIME('now') WHERE id IN (:alarmIds)")
    void markAlarmsSynced(List<Integer> alarmIds);

    @Query("DELETE FROM AlarmReaction WHERE AlarmReaction.id =:alarmId")
    void delete(int alarmId);
}
