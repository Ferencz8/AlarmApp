package countingsheep.alarm.core.services;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;

@Singleton
public class AlarmReactionServiceImpl implements AlarmReactionService {

    private AlarmReactionRepository alarmReactionRepository;
    private AlarmRepository alarmRepository;
    private MessageService messageService;
    private TimeService timeService;
    private PaymentService paymentService;
    private SharedPreferencesContainer sharedPreferencesContainer;

    @Inject
    public AlarmReactionServiceImpl(AlarmReactionRepository alarmReactionRepository,
                                    AlarmRepository alarmRepository,
                                    MessageService messageService,
                                    TimeService timeService,
                                    PaymentService paymentService,
                                    SharedPreferencesContainer sharedPreferencesContainer) {
        this.alarmReactionRepository = alarmReactionRepository;
        this.alarmRepository = alarmRepository;
        this.messageService = messageService;
        this.timeService = timeService;
        this.paymentService = paymentService;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
    }

    @Override
    public void add(final int alarmId, boolean isSnooze, final OnAsyncResponse<Message> onAsyncResponse) {
        final AlarmReaction alarmReaction = new AlarmReaction();

        alarmReaction.setReactedAt(this.timeService.getUTCDateNow());
        alarmReaction.setAlarmId(alarmId);
        alarmReaction.setSnooze(isSnooze);

        this.alarmRepository.get(alarmId, alarmAsResponse -> {

            alarmReaction.setCurrentHour(alarmAsResponse.getHour());
            alarmReaction.setCurrentMinutes(alarmAsResponse.getMinutes());
            alarmReactionRepository.insert(alarmReaction, alarmReactionAsResponse -> {

                if(isSnooze) {
                    int freeCreditsAmount = this.sharedPreferencesContainer.getFreeCredits();

                    if(freeCreditsAmount > 0) {
                        this.sharedPreferencesContainer.setFreeCredits(--freeCreditsAmount);
                    }
                    else{
                        requestSnoozePayment(alarmReactionAsResponse.intValue());
                    }
                }
            });
        });
    }

    public void requestSnoozePayment(int alarmReactionId) {
        paymentService.processPayment(alarmReactionId, new OnResult() {
            @Override
            public void onSuccess(Object result) {
                AlarmReaction alarmReaction = alarmReactionRepository.get(alarmReactionId);
                alarmReaction.setPaymentRequested(true);
            }

            @Override
            public void onFailure() {

            }
        });
        //the alarm reaction id might not be on the server yet, in which case it needs to be added
        //verify if it exists on server using isSync
        //also verify before doing any server call if there is internet
        //if not then mark as not processed the reaction
    }

    @Override
    public List<AlarmReaction> getAllUnsynced() {
        try{
            return this.alarmReactionRepository.getAllUnsynced();
        }
        catch (Exception e){
            //TODO:: log
            return null;
        }
    }

    @Override
    public boolean markSyncedRange(List<AlarmReaction> unsyncedAlarmsReactions) {
        try {

            List<Integer> alarmReactionIds = new ArrayList<>();
            for (AlarmReaction alarmReaction: unsyncedAlarmsReactions) {
                alarmReactionIds.add(alarmReaction.getId());
            }

            this.alarmReactionRepository.markAlarmsSynced(alarmReactionIds);

            return true;
        } catch (Exception exception) {
            //TODO:: add logging
            return false;
        }
    }
}
