package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.AlarmHistoryEmbedded;
import countingsheep.alarm.db.entities.AlarmReaction;

public interface AlarmReactionRepository {

    void insert(AlarmReaction alarmReaction);

    void insert(AlarmReaction alarmReaction, OnAsyncResponse<Long> onAsyncResponse);

    void update(AlarmReaction alarmReaction);

    AlarmReaction get(int id);

    List<AlarmReaction> get();

    List<AlarmReaction> getAllUnsynced();

    void markAlarmsSynced(List<Integer> alarmReactionIds);

    void getCountAlarms(OnAsyncResponse<Integer> response);

    void getSnoozeRate(OnAsyncResponse<Integer> response);

    void getAllAlarms(OnAsyncResponse<List<AlarmHistoryEmbedded>> reactionOnAsyncResponse);
}
