package countingsheep.alarm.network.httpservices;

import android.app.Activity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;
import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.domain.UserRegistration;
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

        this.register(user, null);
    }

    @Override
    public void register(User user, OnSocialLoginResult onSocialLoginResult) {

        retrofit.create(UserAPI.class).register(user).enqueue(new Callback<UserRegistration>() {
            @Override
            public void onResponse(Call<UserRegistration> call, Response<UserRegistration> response) {

                try {
                    UserRegistration registeredUser = response.body();
                    if(registeredUser!=null) {

                        sharedPreferencesContainer.setFullname(registeredUser.getFullname());
                        sharedPreferencesContainer.setProfilePictureUrl(registeredUser.getProfilePictureUrl());

                        sharedPreferencesContainer.setDisplayPaymentInOnBoarding(true);
                        sharedPreferencesContainer.allowFreeCredits(true);

                        sharedPreferencesContainer.setMoneySpentOnSnooze(registeredUser.getMoneySpentOnSnooze());

                        //todo:: hot fix
                        user.id = registeredUser.getUserId();
                        if(onSocialLoginResult!=null) {
                            onSocialLoginResult.onSuccess(user);
                        }
                    }
                } catch (NumberFormatException nfe) {
                    Crashlytics.logException(nfe);
                    Toast.makeText(activity, "Failed to connect to server!", Toast.LENGTH_LONG).show();
                    if(onSocialLoginResult!=null) {
                        onSocialLoginResult.onError(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRegistration> call, Throwable t) {
                if(onSocialLoginResult!=null) {
                    onSocialLoginResult.onError(null);
                }
            }
        });
    }
}
