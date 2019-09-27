package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.Comment;
import countingsheep.alarm.core.domain.SavePhoneNoReq;
import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.domain.UserRegistration;
import countingsheep.alarm.core.domain.FeatureDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {


    @POST("/api/user/register")
    //@POST("/api/user/register")
    Call<UserRegistration> register(@Body User user);

    @Headers({"Accept: application/json"})
    @GET("/api/user/GetCredits")
    Call<Integer> getFreeCredits(@Query("id")int userId);

    @POST("/api/user/saveNumber")
    Call<Void> savePhoneNumber(@Body SavePhoneNoReq savePhoneNoReq);

    @POST("/api/user/AddComment")
        //@POST("/api/user/register")
    Call<Void> addComment(@Body Comment comment);

    @POST("/api/user/UpdateCredits")
    Call<Void> updateCredits(@Query("userId")int userId, @Query("credits") int credits, @Query("isEternal")boolean isEternal);

    @POST("/api/User/AddFeatureReaction")
    Call<Void> addFeature(@Body FeatureDto featureDto);
}
