package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.entities.Alarm;

public interface AlarmService {

    void add(Alarm alarm);

    void add(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse);

    boolean delete(int alarmId);

    boolean update(Alarm alarm);

    Alarm get(int alarmId);

    List<Alarm> getAll();

    void getAll(OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> getAllUnSynced();

    boolean markSyncedRange(List<Alarm> alarms);

    boolean markSynced(Alarm alarm);

    void switchOnOf(int alarmId, boolean value);
}
