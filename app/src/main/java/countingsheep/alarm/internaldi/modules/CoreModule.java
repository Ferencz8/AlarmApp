package countingsheep.alarm.internaldi.modules;

import javax.inject.Singleton;

import countingsheep.alarm.core.services.AlarmReactionServiceImpl;
import countingsheep.alarm.core.services.AlarmServiceImpl;
import countingsheep.alarm.core.services.AuthenticationServiceImpl;
import countingsheep.alarm.core.services.MessageServiceImpl;
import countingsheep.alarm.core.services.PaymentServiceImpl;
import countingsheep.alarm.core.services.SMSServiceImpl;
import countingsheep.alarm.core.services.UserServiceImpl;
import countingsheep.alarm.core.services.interfaces.AlarmReactionService;
import countingsheep.alarm.core.services.interfaces.AlarmService;
import countingsheep.alarm.core.services.interfaces.AuthenticationService;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.core.services.interfaces.PaymentService;
import countingsheep.alarm.core.services.interfaces.SMSService;
import countingsheep.alarm.core.services.interfaces.UserService;
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

    @Provides
    @Singleton
    PaymentService providesPaymentService(PaymentServiceImpl paymentService){return paymentService;}

    @Provides
    @Singleton
    SMSService providesSMSService(SMSServiceImpl smsServiceImpl){return smsServiceImpl;}

    @Provides
    @Singleton
    UserService providesUserService(UserServiceImpl userServiceImpl){return userServiceImpl;}
}
