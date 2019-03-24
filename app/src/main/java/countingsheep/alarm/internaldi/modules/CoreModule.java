package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.services.AlarmReactionServiceImpl;
import countingsheep.alarm.core.services.AlarmServiceImpl;
import countingsheep.alarm.core.services.AuthenticationServiceImpl;
import countingsheep.alarm.core.services.MessageServiceImpl;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

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

    @Provides
    @Singleton
    AlarmReactionService providesAlarmReactionService(AlarmReactionServiceImpl alarmReactionService){return alarmReactionService;}

    @Provides
    @Singleton
    MessageService providesMessageService(MessageServiceImpl messageService){return messageService;}
}
