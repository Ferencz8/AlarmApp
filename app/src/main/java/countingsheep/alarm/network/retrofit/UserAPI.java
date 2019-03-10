package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @POST("/alarm/api/user/register")
    Call<String> register(@Body User user);
}
