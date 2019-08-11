package countingsheep.alarm.core.services;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.db.entities.Alarm;

@Singleton
public class AlarmServiceImpl implements AlarmService {

    private AlarmRepository alarmRepository;
    private TimeService timeService;

    @Inject
    public AlarmServiceImpl(AlarmRepository alarmRepository,
                            TimeService timeService) {
        this.alarmRepository = alarmRepository;
        this.timeService = timeService;
    }

    @Override
    public void add(Alarm alarm) {
        try {
            alarmRepository.insert(alarm);
        }
        catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public void add(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse) {
        try {
            //TODO:: add logic to process failed payments

            alarm.setDateCreated(timeService.getUTCDateNow());
            alarmRepository.insert(alarm, onAsyncResponse);
        }
        catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public boolean delete(int alarmId) {
        try {
            alarmRepository.delete(alarmId);
            return true;
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
            return false;
        }
    }

    @Override
    public boolean update(Alarm alarm) {

        boolean result = true;
        try{

            //TODO:: add logic to process failed payments

            alarm.setDateModified(timeService.getUTCDateNow());
            alarm.setSynced(false);//this way a new alarm will be added on Server
            alarmRepository.update(alarm);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
            result = false;
        }
        return result;
    }

    @Override
    public void update(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse) {

        try{
            //add a new one
            Alarm newAlarm = new Alarm();
            newAlarm.setDateCreated(timeService.getUTCDateNow());
            newAlarm.setHour(alarm.getHour());
            newAlarm.setMinutes(alarm.getMinutes());
            newAlarm.setRepeatDays(alarm.getRepeatDays());
            newAlarm.setRingtoneName(alarm.getRingtoneName());
            newAlarm.setRingtonePath(alarm.getRingtonePath());
            newAlarm.setSnoozeAmount(alarm.getSnoozeAmount());
            newAlarm.setTitle(alarm.getTitle());
            newAlarm.setVolume(alarm.getVolume());
            newAlarm.setVobrateOn(alarm.isVobrateOn());
            newAlarm.setTurnedOn(true);

            //remove the old one
            alarm.setDateModified(timeService.getUTCDateNow());
            alarm.setDeleted(true);
            alarmRepository.update(alarm);

            alarmRepository.insert(newAlarm, onAsyncResponse);

        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public void get(int alarmId, OnAsyncResponse<Alarm> onAsyncResponse) {
        try {
            this.alarmRepository.get(alarmId, onAsyncResponse);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public Alarm get(int alarmId) {
        try {
            return this.alarmRepository.get(alarmId);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
            return null;
        }
    }

    @Override
    public List<Alarm> getAll() {
        return this.alarmRepository.get();
    }

    @Override
    public void getAll(OnAsyncResponse<List<Alarm>> onAsyncResponse) {
        this.alarmRepository.getAllNotDeleted(onAsyncResponse);
    }

    @Override
    public List<Alarm> getAllUnSynced() {
        return this.alarmRepository.getAllUnsyced();
    }

    @Override
    public boolean markSyncedRange(List<Alarm> alarms) {
        try {

            List<Integer> alarmIds = new ArrayList<>();
            for (Alarm alarm: alarms) {
                alarmIds.add(alarm.getId());
            }

            this.alarmRepository.markAlarmsSynced(alarmIds);

            return true;
        } catch (Exception exception) {
            Crashlytics.logException(exception);
            return false;
        }
    }

    @Override
    public boolean markSynced(Alarm alarm) {
        try {

            alarm.setSynced(true);
            alarm.setDateModified(this.timeService.getUTCDateNow());
            this.alarmRepository.update(alarm);

            return true;
        } catch (Exception exception) {
            Crashlytics.logException(exception);
            return false;
        }
    }

    @Override
    public void switchOnOf(int alarmId, boolean value) {
        this.alarmRepository.get(alarmId, new OnAsyncResponse<Alarm>() {
            @Override
            public void processResponse(Alarm response) {

                response.setTurnedOn(value);
                alarmRepository.update(response);
            }
        });
    }

    @Override
    public List<Alarm> getOnOrOff(boolean state) {
        try{

            return this.alarmRepository.getOnOrOffAlarms(state);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
            return null;
        }
    }

    @Override
    public void getOnOrOff(OnAsyncResponse<List<Alarm>> onAsyncResponse, boolean state) {
        try{

            this.alarmRepository.getOnOrOffAlarms(onAsyncResponse, state);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public void getSnoozesCount(int alarmId, OnAsyncResponse<Integer> onAsyncResponse){
        try{

            this.alarmRepository.getSnoozesCount(alarmId, onAsyncResponse);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
    }

    @Override
    public void get(int hour, int minutes, OnAsyncResponse<List<Alarm>> onAsyncResponse) {
        try{
            int minMinutes, minHour, maxHour, maxMinutes;

            if(minutes < 30){
                minMinutes = maxMinutes = 30 + minutes;
                minHour = hour - 1;
                maxHour = hour;
            }
            else {
                minMinutes = maxMinutes = minutes - 30;
                minHour = hour;
                maxHour = hour + 1;
            }

            this.alarmRepository.getAlarm(minHour, maxHour, minMinutes, maxMinutes, onAsyncResponse);
        }
        catch(Exception exception){
            Crashlytics.logException(exception);
        }
    }
}
