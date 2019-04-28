package countingsheep.alarm.core.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
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
            //TODO:: add logging
        }
    }

    @Override
    public void add(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse) {
        try {
            alarm.setDateCreated(timeService.getUTCDateNow());
            alarmRepository.insert(alarm, onAsyncResponse);
        }
        catch (Exception exception){
            //TODO:: add logging
        }
    }

    @Override
    public boolean delete(int alarmId) {
        try {
            alarmRepository.delete(alarmId);
            return true;
        }
        catch(Exception exception){

            return false;
        }
    }

    @Override
    public boolean update(Alarm alarm) {

        boolean result = true;
        try{
            alarm.setDateModified(timeService.getUTCDateNow());
            alarm.setSynced(false);//this way a new alarm will be added on Server
            alarmRepository.update(alarm);
        }
        catch(Exception exception){
            //TODO:: add logging
            result = false;
        }
        return result;
    }

    @Override
    public void get(int alarmId, OnAsyncResponse<Alarm> onAsyncResponse) {
        try {
            this.alarmRepository.get(alarmId, onAsyncResponse);
        }
        catch(Exception exception){
            //TODO:: add logging
        }
    }

    @Override
    public Alarm get(int alarmId) {
        try {
            return this.alarmRepository.get(alarmId);
        }
        catch(Exception exception){
            //TODO:: add logging
            return null;
        }
    }

    @Override
    public List<Alarm> getAll() {
        return this.alarmRepository.get();
    }

    @Override
    public void getAll(OnAsyncResponse<List<Alarm>> onAsyncResponse) {
        this.alarmRepository.get(onAsyncResponse);
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
            //TODO:: add logging
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
            //TODO:: add logging
            return false;
        }
    }

    @Override
    public void switchOnOf(int alarmId, boolean value) {
        Alarm alarmModel = this.alarmRepository.get(alarmId);
        alarmModel.setTurnedOn(value);
        this.alarmRepository.update(alarmModel);
    }

    @Override
    public List<Alarm> getOnOrOff(boolean state) {
        try{

            return this.alarmRepository.getOnOrOffAlarms(state);
        }
        catch(Exception exception){
            return null;
        }
    }

    @Override
    public void getOnOrOff(OnAsyncResponse<List<Alarm>> onAsyncResponse, boolean state) {
        try{

            this.alarmRepository.getOnOrOffAlarms(onAsyncResponse, state);
        }
        catch(Exception exception){

        }
    }
}
