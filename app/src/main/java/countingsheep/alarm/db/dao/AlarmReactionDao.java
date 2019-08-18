package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.AlarmHistoryEmbedded;
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

    @Query("SELECT COUNT(*) FROM AlarmReaction WHERE isSnooze =:isSnooze")
    Integer getAllAlarmsCount(boolean isSnooze);

    @Query("SELECT COUNT(*) FROM AlarmReaction")
    Integer getAllAlarmsCount();
//
//        @Query("SELECT AlarmReaction.currentHour AS hour, " +
//            "AlarmReaction.currentMinutes AS minute, " +
//            "AlarmReaction.isSnooze AS isSnooze, " +
//            "Alarm.title AS name, " +
//            "Alarm.dateCreated AS createdDate, " +
//            "PaymentDetails.amount AS amount " +
//                "FROM Alarm " +
//            "INNER JOIN AlarmReaction ON AlarmReaction.alarmId = Alarm.id " +
//            "INNER JOIN PaymentDetails ON PaymentDetails.alarmReactionId = AlarmReaction.id")
//    List<AlarmHistoryEmbedded> getAllAlarmHistory();


    @Query("SELECT AlarmReaction.id AS ar_id , " +
            "AlarmReaction.currentMinutes AS ar_currentMinutes, " +
            "AlarmReaction.isSnooze AS ar_isSnooze, " +
            "Alarm.*, " +
            "PaymentDetails.amount AS pd_amount " +
            "FROM Alarm " +
            "INNER JOIN AlarmReaction ON AlarmReaction.alarmId = Alarm.id " +
            "LEFT JOIN PaymentDetails ON PaymentDetails.alarmReactionId = AlarmReaction.id " +
            "ORDER BY AlarmReaction.reactedAt DESC")
    List<AlarmHistoryEmbedded> getAllAlarmHistory();
}
