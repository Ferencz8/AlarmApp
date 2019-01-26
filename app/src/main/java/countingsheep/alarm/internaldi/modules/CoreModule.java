package countingsheep.alarm.internaldi.modules;

import android.app.Activity;

import javax.inject.Singleton;

import countingsheep.alarm.core.datainterfaces.ApiAuthenticationService;
import countingsheep.alarm.core.datainterfaces.SocialAuthenticationService;
import countingsheep.alarm.core.services.AlarmServiceImpl;
import countingsheep.alarm.core.services.AuthenticationServiceImpl;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import dagger.Module;
import dagger.Provides;

@Module(includes = {DataAccessModule.class})
public class CoreModule {

    //    @Provides
//    @Singleton
//    AuthenticationService providesAuthenticationService(SocialAuthenticationService socialAuthenticationService,
//                                                        ApiAuthenticationService apiAuthenticationService){
//        return new AuthenticationServiceImpl(socialAuthenticationService, apiAuthenticationService);
//    }
    @Provides
    @Singleton
    AuthenticationService providesAuthenticationService(AuthenticationServiceImpl authenticationService) {
        return authenticationService;
    }

    @Provides
    @Singleton
    AlarmService providesAlarmService(AlarmServiceImpl alarmService){
        return alarmService;
    }
}
