package countingsheep.alarm.core.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.contracts.data.AlarmRepository;

@Singleton
public class AlarmServiceImpl implements AlarmService {

    private AlarmRepository alarmRepository;

    @Inject
    public AlarmServiceImpl(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public void add(AlarmModel alarm) {
        alarmRepository.insert(alarm);
    }

    @Override
    public boolean delete(int alarmId) {
        return false;
    }

    @Override
    public boolean update(AlarmModel alarm) {
        return false;
    }

    @Override
    public AlarmModel get(int alarmId) {
        return this.alarmRepository.get(alarmId);
    }

    @Override
    public List<AlarmModel> getAll() {
        return this.alarmRepository.get();
    }

    @Override
    public void switchOnOf(int alarmId, boolean value) {
        AlarmModel alarmModel = this.alarmRepository.get(alarmId);
        alarmModel.setTurnedOn(value);
        this.alarmRepository.update(alarmModel);
    }
}
