package countingsheep.alarm.dataaccess.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Message;

@Dao
public interface MessageDao extends BaseDao<Message>{

    @Query("SELECT * FROM Message WHERE isSeen = 0 LIMIT 1")
    Message getNotSeen();

    @Query("SELECT * FROM Message")
    List<Message> getAll();

    @Query("SELECT * FROM Message WHERE id =:messageId")
    Message getById(int messageId);

    @Query("SELECT * FROM Message WHERE isSeen = 1 AND isSynced =:seenValue")
    List<Message> getAllUnsynced(boolean seenValue);

    @Query("UPDATE Alarm SET IsSynced = 1, DateModified = DATETIME('now') WHERE id IN (:messageIds)")
    void markMessagesSynced(List<Integer> messageIds);
}
