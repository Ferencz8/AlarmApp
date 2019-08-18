package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.db.entities.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageAPI {

    //Used to update messages once they have been seen with properties such as dateseen, liked and shared
    //@POST("/alarm/api/message")
    @POST("/alarm/api/message")
    Call<String> markMessages(@Body UserWrappedEntities<Message> messages);


    @Headers({"Accept: application/json"})
    @GET("/alarm/api/message/GetOneForUser")
    Call<Message> getRoastMessage(@Query("userId")int userId);
}
