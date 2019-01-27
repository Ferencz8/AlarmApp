package countingsheep.alarm.dataaccess.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.AlarmReaction;

@Dao
public interface AlarmReactionDao extends BaseDao<AlarmReaction> {

    @Query("SELECT * FROM AlarmReaction")
    List<AlarmReaction> getAll();

    @Query("SELECT * FROM AlarmReaction WHERE id =:alarmReactionId")
    AlarmReaction getById(int alarmReactionId);
}
