package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.Alarm;

public interface AlarmRepository {

    void insert(Alarm alarm);

    void insert(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse);

    void update(Alarm alarm);

    Alarm get(int id);

    List<Alarm> get();

    void get(OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> getAllUnsyced();

    void markAlarmsSynced(List<Integer> alarmIds);
}
