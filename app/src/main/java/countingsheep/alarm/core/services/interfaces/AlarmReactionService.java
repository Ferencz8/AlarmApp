package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;

public interface AlarmReactionService {

    /*Depending on the type of monetization the user has this can give back a Message if the
    HateMessage/Snooze monetization type is chosen or null if the $/Snooze; in which case it will
    also attempt to withdraw the currently chosen amount of $/Snooze specified
    */
    void add(int alarmId, boolean isSnooze, OnAsyncResponse<Message> messageOnAsyncResponse);

    List<AlarmReaction> getAllUnsynced();

    boolean markSyncedRange(List<AlarmReaction> unsyncedAlarmsReactions);

    void requestSnoozePayment(int alarmReactionId);
}
