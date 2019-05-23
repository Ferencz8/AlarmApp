package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.db.entities.Alarm;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlarmAPI {

    //@POST("/alarm/api/alarm")
    @POST("/alarm/api/alarm")
    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> addAlarm(@Body UserWrappedEntity<Alarm> alarm);

    //@POST("/alarm/api/alarm/addmany")
    @POST("/alarm/api/alarm/addmany")
        //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> addAlarmRange(@Body UserWrappedEntities<Alarm> alarms);
}
