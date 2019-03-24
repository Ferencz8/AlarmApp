package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.AlarmReactionRepository;
import countingsheep.alarm.core.contracts.data.AlarmRepository;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.core.contracts.data.MessageRepository;
import countingsheep.alarm.db.repositories.AlarmReactionRepositoryImpl;
import countingsheep.alarm.db.repositories.MessageRepositoryImpl;
import countingsheep.alarm.network.httpservices.ApiAuthenticationServiceImpl;
import countingsheep.alarm.network.httpservices.FacebookAuthenticationServiceImpl;
import countingsheep.alarm.db.repositories.AlarmRepositoryImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = { DatabaseModule.class})
public class DataAccessModule {

    @Provides
    @Singleton
    AlarmRepository providesAlarmRepository(AlarmRepositoryImpl alarmRepository) {
        return alarmRepository;
    }

    @Provides
    @Singleton
    AlarmReactionRepository providesAlarmReactionRepository(AlarmReactionRepositoryImpl alarmReactionRepository){return alarmReactionRepository;}

    @Provides
    @Singleton
    MessageRepository providesMessageRepository(MessageRepositoryImpl messageRepository){return messageRepository;}
}
