package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.domain.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {


    @POST("/alarm/api/user/register")
    //@POST("/api/user/register")
    Call<UserRegistration> register(@Body User user);
}
