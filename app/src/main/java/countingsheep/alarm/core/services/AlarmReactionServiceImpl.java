package countingsheep.alarm.core.services;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.db.entities.AlarmHistoryEmbedded;
import countingsheep.alarm.db.entities.AlarmReaction;
import countingsheep.alarm.db.entities.Message;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
import countingsheep.alarm.ui.settings.models.AlarmHistory;
import countingsheep.alarm.util.StringFormatter;

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
    public void add(final int alarmId, boolean isSnooze, final OnAsyncResponse<Void> onAsyncResponse) {
        try {
            final AlarmReaction alarmReaction = new AlarmReaction();

            alarmReaction.setReactedAt(this.timeService.getUTCDateNow());
            alarmReaction.setAlarmId(alarmId);
            alarmReaction.setSnooze(isSnooze);


            this.alarmRepository.get(alarmId, alarmAsResponse -> {

                alarmReaction.setCurrentHour(alarmAsResponse.getHour());
                alarmReaction.setCurrentMinutes(alarmAsResponse.getMinutes());

                if (isSnooze) {
                    int freeCreditsAmount = this.sharedPreferencesContainer.getFreeCredits();

                    if (freeCreditsAmount > 0) {
                        this.sharedPreferencesContainer.setFreeCredits(--freeCreditsAmount);
                    } else {
                        alarmReaction.setPayable(true);
                        alarmReactionRepository.insert(alarmReaction, dbAlarmReactionId ->
                                requestSnoozePayment(dbAlarmReactionId.intValue())
                        );
                    }
                } else {
                    alarmReactionRepository.insert(alarmReaction);
                }
            });
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
        finally {
            onAsyncResponse.processResponse(null);
        }
    }

    public void requestSnoozePayment(int alarmReactionId) {
        paymentService.processPayment(alarmReactionId,null);
        //the alarm reaction id might not be on the server yet, in which case it needs to be added
        //verify if it exists on server using isSync
        //also verify before doing any server call if there is internet
        //if not then mark as not processed the reaction
    }

    @Override
    public void getAlarmsCount(OnAsyncResponse<Integer> response) {
        this.alarmReactionRepository.getCountAlarms(response);
    }

    @Override
    public void getSnoozeRate(OnAsyncResponse<Integer> response) {
        this.alarmReactionRepository.getSnoozeRate(response);
    }

    @Override
    public void getAllAlarmReactionHistory(OnAsyncResponse<List<AlarmHistory>> reactionOnAsyncResponse) {
        //this.alarmReactionRepository.getAllAlarms(reactionOnAsyncResponse);
        this.alarmReactionRepository.getAllAlarms(new OnAsyncResponse<List<AlarmHistoryEmbedded>>() {
            @Override
            public void processResponse(List<AlarmHistoryEmbedded> response) {

                ArrayList<AlarmHistory> alarmHistories = new ArrayList<>();
//                for (AlarmHistoryEmbedded alarmHistoryEmbedded : response){
//                    try {
//                        AlarmHistory alarmHistory = new AlarmHistory();
//                        alarmHistory.setName(alarmHistoryEmbedded.getName());
//                        alarmHistory.setCreatedDate(String.valueOf(alarmHistoryEmbedded.getCreatedDate()));
//                        alarmHistory.setCreatedHour(String.valueOf(StringFormatter.getFormattedTimeDigits(alarmHistoryEmbedded.getHour())) + ":" + StringFormatter.getFormattedTimeDigits(alarmHistoryEmbedded.getMinute()));
//                        alarmHistory.setCashSpent(alarmHistoryEmbedded.getAmount());
//                        String reactionType = alarmHistoryEmbedded.isSnooze() ? "Snooze" : "Awake";
//                        alarmHistory.setReactionType(reactionType);
//                        alarmHistory.setRequireRefund(false);
//                        alarmHistories.add(alarmHistory);
//                    } catch (Exception e){
//                        Crashlytics.logException(e);
//                    }
//                }
                for (AlarmHistoryEmbedded alarmHistoryEmbedded : response){
                    try {
                        AlarmHistory alarmHistory = new AlarmHistory();
                        alarmHistory.setName(alarmHistoryEmbedded.getAlarm().getTitle());
                        alarmHistory.setCreatedDate(String.valueOf(alarmHistoryEmbedded.getAlarm().getDateCreated()));
                        alarmHistory.setCreatedHour(String.valueOf(StringFormatter.getFormattedTimeDigits(alarmHistoryEmbedded.getAlarmReaction().getCurrentHour())) + ":" +
                                StringFormatter.getFormattedTimeDigits(alarmHistoryEmbedded.getAlarmReaction().getCurrentMinutes()));

                        Integer cashSpent = alarmHistoryEmbedded.getPaymentDetails()!=null ? alarmHistoryEmbedded.getPaymentDetails().getAmount() : 0;
                        alarmHistory.setCashSpent(cashSpent);
                        String reactionType = alarmHistoryEmbedded.getAlarmReaction().isSnooze() ? "Snooze" : "Awake";
                        alarmHistory.setReactionType(reactionType);
                        alarmHistory.setRequireRefund(false);
                        alarmHistories.add(alarmHistory);
                    } catch (Exception e){
                        Crashlytics.logException(e);
                    }
                }
                reactionOnAsyncResponse.processResponse(alarmHistories);
            }
        });
    }

    @Override
    public List<AlarmReaction> getAllUnsynced() {
        try {
            return this.alarmReactionRepository.getAllUnsynced();
        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    @Override
    public boolean markSyncedRange(List<AlarmReaction> unsyncedAlarmsReactions) {
        try {

            List<Integer> alarmReactionIds = new ArrayList<>();
            for (AlarmReaction alarmReaction : unsyncedAlarmsReactions) {
                alarmReactionIds.add(alarmReaction.getId());
            }

            this.alarmReactionRepository.markAlarmsSynced(alarmReactionIds);

            return true;
        } catch (Exception exception) {
            Crashlytics.logException(exception);
            return false;
        }
    }
}
