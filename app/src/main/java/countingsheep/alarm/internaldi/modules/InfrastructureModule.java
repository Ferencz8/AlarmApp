package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.infrastructure.EMailService;
import countingsheep.alarm.infrastructure.EMailServiceImpl;
import countingsheep.alarm.infrastructure.NetworkInteractor;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class})
public class InfrastructureModule {

    @Provides
    @Singleton
    EMailService providesEMailService(EMailServiceImpl eMailService) {
        return eMailService;
    }
}
