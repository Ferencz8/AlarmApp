package countingsheep.alarm.core.services;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;

@Singleton
public class AlarmReactionServiceImpl implements AlarmReactionService {

    private AlarmReactionRepository alarmReactionRepository;
    private MessageService messageService;
    private TimeService timeService;
    private SharedPreferences sharedPreferences;

    @Inject
    public AlarmReactionServiceImpl(AlarmReactionRepository alarmReactionRepository,
                                    MessageService messageService,
                                    TimeService timeService,
                                    SharedPreferences sharedPreferences) {
        this.alarmReactionRepository = alarmReactionRepository;
        this.messageService = messageService;
        this.timeService = timeService;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Message add(int alarmId, boolean isSnooze) {
        AlarmReaction alarmReaction = new AlarmReaction();

        alarmReaction.setReactedAt(this.timeService.getUTCDateNow());
        alarmReaction.setAlarmId(alarmId);
        alarmReaction.setSnooze(isSnooze);
        this.alarmReactionRepository.insert(alarmReaction);
        //todo check type of monetization
        if(this.sharedPreferences.getInt("MonetizationType", 0) == 0) {
            //case hate
            return this.messageService.getUnseenMessage();

        }
        else {

            //case $$
            return null;
        }
    }
}
