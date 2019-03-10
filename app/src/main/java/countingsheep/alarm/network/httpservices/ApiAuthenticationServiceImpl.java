package countingsheep.alarm.network.httpservices;

import android.app.Activity;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.network.retrofit.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class ApiAuthenticationServiceImpl implements ApiAuthenticationService {

    private Activity activity;
    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;

    @Inject
    public ApiAuthenticationServiceImpl(Activity activity,
                                        Retrofit retrofit,
                                        SharedPreferencesContainer sharedPreferencesContainer) {
        this.activity = activity;
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
    }

    @Override
    public void register(User user) {

        retrofit.create(UserAPI.class).register(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    int userId = Integer.parseInt(response.body());
                    sharedPreferencesContainer.setCurrentUserId(userId);

                } catch (NumberFormatException nfe) {
                    Toast.makeText(activity, "Failed to connect to server!", Toast.LENGTH_LONG).show();
                    //TODO:: send a Server failed to connect error
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
