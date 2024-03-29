package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.db.entities.AlarmReaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlarmReactionAPI {


    //@POST("/api")
    @POST("/api/alarmreaction")
    Call<String> addAlarm(@Body UserWrappedEntity<AlarmReaction> wrappedAlarmReaction);

    //@POST("/api/addmany")
    @POST("/api/alarmreaction/addmany")
    Call<String> addAlarmReactionRange(@Body UserWrappedEntities<AlarmReaction> wrappedAlarmReactions);
}
