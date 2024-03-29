package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.db.entities.Alarm;

public interface AlarmRepository {

    void insert(Alarm alarm);

    void  insert(Alarm alarm, OnAsyncResponse<Long> onAsyncResponse);

    void update(Alarm alarm);

    void update(Alarm alarm, OnAsyncResponse<Void> onAsyncResponse);

    void delete(int alarmId);

    Alarm get(int id);

    Alarm getByAlarmReactionId(int alarmReactionId);

    void get(int id, OnAsyncResponse<Alarm> onAsyncResponse);

    void getAlarm(int minHour, int maxHour, int minMinutes, int maxMinutes, OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> get();

    void get(OnAsyncResponse<List<Alarm>> onAsyncResponse);

    List<Alarm> getAllUnsyced();

    List<Alarm> getOnOrOffAlarms(boolean state);

    void getOnOrOffAlarms(OnAsyncResponse<List<Alarm>> onAsyncResponse, boolean state);

    void markAlarmsSynced(List<Integer> alarmIds);

    void getAllNotDeleted(OnAsyncResponse<List<Alarm>> onAsyncResponse);

    //void getSnoozesCount(int alarmId);

    void getSnoozesCount(int alarmId, OnAsyncResponse<Integer> onAsyncResponse);
}
