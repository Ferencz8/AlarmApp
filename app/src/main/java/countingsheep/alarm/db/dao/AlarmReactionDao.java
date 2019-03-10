package countingsheep.alarm.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.AlarmReaction;

@Dao
public interface AlarmReactionDao extends BaseDao<AlarmReaction> {

    @Query("SELECT * FROM AlarmReaction")
    List<AlarmReaction> getAll();

    @Query("SELECT * FROM AlarmReaction WHERE id =:alarmReactionId")
    AlarmReaction getById(int alarmReactionId);
}
