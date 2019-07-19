package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.domain.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {


    @POST("/alarm/api/user/register")
    //@POST("/alarm/api/user/register")
    Call<UserRegistration> register(@Body User user);

    @Headers({"Accept: application/json"})
    @GET("/alarm/api/user/GetCredits")
    Call<Integer> getFreeCredits(@Query("id")int userId);
}
