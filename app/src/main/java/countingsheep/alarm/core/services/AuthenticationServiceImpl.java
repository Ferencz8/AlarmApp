package countingsheep.alarm.core.services;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

    private SocialAuthenticationService socialAuthenticationService;
    private ApiAuthenticationService apiAuthenticationService;

    @Inject
    public AuthenticationServiceImpl(SocialAuthenticationService socialAuthenticationService,
                                     ApiAuthenticationService apiAuthenticationService) {
        this.socialAuthenticationService = socialAuthenticationService;
        this.apiAuthenticationService = apiAuthenticationService;
    }

    @Override
    public void socialLogin(final OnSocialLoginResult onResult) {

        this.socialAuthenticationService.registerCallback(onResult);

        //this.socialAuthenticationService.registerCallback(remotelyRegisterUser());

        this.socialAuthenticationService.login();
    }

    @NonNull
    private OnSocialLoginResult remotelyRegisterUser() {
        return new OnSocialLoginResult(){

            @Override
            public void onSuccess(User user) {

                //todo maybe do on a thread async
                apiAuthenticationService.register(user);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(Exception exception) {

            }
        };
    }
}
