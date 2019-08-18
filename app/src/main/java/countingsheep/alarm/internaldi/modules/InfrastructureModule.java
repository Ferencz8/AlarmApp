package countingsheep.alarm.internaldi.modules;

import android.app.Notification;

import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.infrastructure.EMailService;
import countingsheep.alarm.core.contracts.infrastructure.SMSService;
import countingsheep.alarm.infrastructure.EMailServiceImpl;
import countingsheep.alarm.infrastructure.LocalSMSServiceImpl;
import countingsheep.alarm.infrastructure.NotificationHelper;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class})
public class InfrastructureModule {

    @Provides
    @Singleton
    EMailService providesEMailService(EMailServiceImpl eMailService) {
        return eMailService;
    }


    @Provides
    @Singleton
    SMSService providesLocalSMSService(LocalSMSServiceImpl localSMSService) {
        return localSMSService;
    }
}
