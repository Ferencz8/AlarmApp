package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.db.entities.Message;

public interface AlarmReactionService {

    /*Depending on the type of monetization the user has this can give back a Message if the
    HateMessage/Snooze monetization type is chosen or null if the $/Snooze; in which case it will
    also attempt to withdraw the currently chosen amount of $/Snooze specified
    */
    Message add(int alarmId, boolean isSnooze);
}
