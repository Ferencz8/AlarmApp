package countingsheep.alarm.core.services;

import androidx.annotation.NonNull;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.domain.UserRegistration;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.network.retrofit.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

    private SocialAuthenticationService socialAuthenticationService;
    private ApiAuthenticationService apiAuthenticationService;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private Retrofit retrofit;
    private static final int UserDoesNotExist = 0;

    @Inject
    public AuthenticationServiceImpl(SocialAuthenticationService socialAuthenticationService,
                                     ApiAuthenticationService apiAuthenticationService,
                                     SharedPreferencesContainer sharedPreferencesContainer,
                                     Retrofit retrofit) {
        this.socialAuthenticationService = socialAuthenticationService;
        this.apiAuthenticationService = apiAuthenticationService;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.retrofit = retrofit;
    }

    @Override
    public void socialLogin(final OnSocialLoginResult onResult) {

        if(!sharedPreferencesContainer.doesUserIdExist()){

            //if this is the 1st time the client authenticates,the remote  server register is a MUST
            this.socialAuthenticationService.registerCallback(remotelyRegisterUser(onResult));
        }
        else
        {
            //otherwise Facebook login is enough
            this.socialAuthenticationService.registerCallback(onResult);

            this.getUserFreeCredits();
        }

        this.socialAuthenticationService.login();
    }

    @Override
    public void socialLogout() {
        this.socialAuthenticationService.logout();
    }

    @NonNull
    private OnSocialLoginResult remotelyRegisterUser(final OnSocialLoginResult onResult) {
        return new OnSocialLoginResult(){

            @Override
            public void onSuccess(User user) {

                //todo maybe do on a thread async
                apiAuthenticationService.register(user, onResult);
            }

            @Override
            public void onCancel() {
                onResult.onCancel();
            }

            @Override
            public void onError(Exception exception) {
                onResult.onError(exception);
            }
        };
    }

    private void getUserFreeCredits() {
        this.retrofit.create(UserAPI.class).getFreeCredits(this.sharedPreferencesContainer.getCurrentUserId()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!=0){
                    sharedPreferencesContainer.setFreeCredits(response.body());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }
}
