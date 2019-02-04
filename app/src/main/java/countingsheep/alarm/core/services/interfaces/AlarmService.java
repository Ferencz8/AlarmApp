package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Alarm;

public interface AlarmService {

    void add(Alarm alarm);

    boolean delete(int alarmId);

    boolean update(Alarm alarm);

    Alarm get(int alarmId);

    List<Alarm> getAll();

    List<Alarm> getAllUnSynced();

    boolean markSyncedRange(List<Alarm> alarms);

    boolean markSynced(Alarm alarm);

    void switchOnOf(int alarmId, boolean value);
}
