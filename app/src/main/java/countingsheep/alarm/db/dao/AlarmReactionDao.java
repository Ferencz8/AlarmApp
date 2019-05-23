package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.AlarmReaction;

@Dao
public interface AlarmReactionDao extends BaseDao<AlarmReaction> {

    @Query("SELECT * FROM AlarmReaction")
    List<AlarmReaction> getAll();

    @Query("SELECT * FROM AlarmReaction WHERE id =:alarmReactionId")
    AlarmReaction getById(int alarmReactionId);

    @Query("SELECT * FROM AlarmReaction WHERE isSynced =0")
    List<AlarmReaction> getAllUnSynced();

    @Query("UPDATE AlarmReaction SET IsSynced = 1 WHERE id IN (:alarmReactionIds)")
    void markAlarmReactionsSynced(List<Integer> alarmReactionIds);
}
