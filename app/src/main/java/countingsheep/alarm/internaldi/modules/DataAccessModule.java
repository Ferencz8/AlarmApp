package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.dataaccess.httpservices.ApiAuthenticationServiceImpl;
import countingsheep.alarm.dataaccess.httpservices.FacebookAuthenticationServiceImpl;
import countingsheep.alarm.dataaccess.repositories.AlarmRepositoryImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class, DatabaseModule.class})
public class DataAccessModule {

    @Provides
    @Singleton
    AlarmRepository providesAlarmRepository(AlarmRepositoryImpl alarmRepository) {
        return alarmRepository;
    }

//    @Provides
//    @Singleton
//    AlarmRepository providesAlarmRepository(AlarmDatabase alarmDatabase) {
//        return new AlarmRepositoryImpl(alarmDatabase, new AlarmMapper());
//    }

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

//    @Provides
//    @Singleton
//    SocialAuthenticationService providesSocialAuthenticationService(Activity activity){
//        return new FacebookAuthenticationServiceImpl(activity);
//    }

//    @Provides
//    @Singleton
//    ApiAuthenticationService providesApiAuthenticationService(Activity activity){
//        return new ApiAuthenticationServiceImpl(activity);
//    }
}
