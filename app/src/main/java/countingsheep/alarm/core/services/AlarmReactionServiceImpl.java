package countingsheep.alarm.core.services;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;

@Singleton
public class AlarmReactionServiceImpl implements AlarmReactionService {

    private AlarmReactionRepository alarmReactionRepository;
    private AlarmRepository alarmRepository;
    private MessageService messageService;
    private TimeService timeService;
    private SharedPreferences sharedPreferences;

    @Inject
    public AlarmReactionServiceImpl(AlarmReactionRepository alarmReactionRepository,
                                    AlarmRepository alarmRepository,
                                    MessageService messageService,
                                    TimeService timeService,
                                    SharedPreferences sharedPreferences) {
        this.alarmReactionRepository = alarmReactionRepository;
        this.alarmRepository = alarmRepository;
        this.messageService = messageService;
        this.timeService = timeService;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void add(final int alarmId, boolean isSnooze, final OnAsyncResponse<Message> onAsyncResponse) {
        final AlarmReaction alarmReaction = new AlarmReaction();

        alarmReaction.setReactedAt(this.timeService.getUTCDateNow());
        alarmReaction.setAlarmId(alarmId);
        alarmReaction.setSnooze(isSnooze);

        this.alarmRepository.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm response) {

                alarmReaction.setCurrentHour(response.getHour());
                alarmReaction.setCurrentMinutes(response.getMinutes());
                alarmReactionRepository.insert(alarmReaction);

                onAsyncResponse.processResponse(null);
            }
        });

//        //todo check type of monetization
//        if(this.sharedPreferences.getInt("MonetizationType", 0) == 0) {
//            //case hate
//            return this.messageService.getUnseenMessage();
//
//        }
//        else {

            //case $$
//            return null;
//        }
    }
}
