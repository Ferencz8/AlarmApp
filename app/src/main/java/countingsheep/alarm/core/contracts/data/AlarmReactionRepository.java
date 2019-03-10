package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.AlarmReaction;

public interface AlarmReactionRepository {

    void insert(AlarmReaction alarmReaction);

    void update(AlarmReaction alarmReaction);

    AlarmReaction get(int id);

    List<AlarmReaction> get();
}
