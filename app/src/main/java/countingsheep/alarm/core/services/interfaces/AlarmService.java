package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.core.domain.AlarmModel;

public interface AlarmService {

    void add(AlarmModel alarm);

    boolean delete(int alarmId);

    boolean update(AlarmModel alarm);

    AlarmModel get(int alarmId);

    List<AlarmModel> getAll();

    void switchOnOf(int alarmId, boolean value);
}
