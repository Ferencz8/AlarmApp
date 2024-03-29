package countingsheep.alarm.core.services.interfaces;

import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.entities.Alarm;

public interface AlarmService {

    void add(Alarm alarm);

    void add(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse);

    boolean delete(int alarmId);

    boolean update(Alarm alarm);

    void update(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse);

    Alarm get(int alarmId);

    void get(int alarmId, OnAsyncResponse<Alarm> onAsyncResponse);

    void get(int hour, int minutes, OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> getAll();

    void getAll(OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> getAllUnSynced();

    boolean markSyncedRange(List<Alarm> alarms);

    boolean markSynced(Alarm alarm);

    void switchOnOf(int alarmId, boolean value);

    List<Alarm> getOnOrOff(boolean state);

    void getOnOrOff(OnAsyncResponse<List<Alarm>> onAsyncResponse, boolean state);

    void getSnoozesCount(int alarmId, OnAsyncResponse<Integer> onAsyncResponse);
}
