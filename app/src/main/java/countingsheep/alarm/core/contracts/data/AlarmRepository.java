package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Alarm;

public interface AlarmRepository {

    void insert(Alarm alarm);

    void update(Alarm alarm);

    Alarm get(int id);

    List<Alarm> get();

    List<Alarm> getAllUnsyced();

    void markAlarmsSynced(List<Integer> alarmIds);
}
