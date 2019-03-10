package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.db.entities.Alarm;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlarmAPI {

    @POST("/alarm/api/alarm")
    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> addAlarm(@Body UserWrappedEntity<Alarm> alarm);

    @POST("/alarm/api/alarm/addmany")
        //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> addAlarmRange(@Body UserWrappedEntities<Alarm> alarms);
}
