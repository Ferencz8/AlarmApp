package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.network.httpservices.ApiAuthenticationServiceImpl;
import countingsheep.alarm.network.httpservices.FacebookAuthenticationServiceImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class })
public class NetworkModule {

    @Provides
    @Singleton
    SocialAuthenticationService providesSocialAuthenticationService(FacebookAuthenticationServiceImpl facebookAuthenticationService){
        return facebookAuthenticationService;
    }

    @Provides
    @Singleton
    ApiAuthenticationService providesApiAuthenticationService(ApiAuthenticationServiceImpl apiAuthenticationService){
        return apiAuthenticationService;
    }

}
