package countingsheep.alarm.internaldi.components;


import android.app.Application;

import javax.inject.Singleton;

import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.ApplicationModule;
import countingsheep.alarm.internaldi.modules.BroadcastReceiverModule;
import countingsheep.alarm.internaldi.modules.ServiceModule;
import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    BroadcastReceiverComponent newBroadcastReceiverComponent(BroadcastReceiverModule broadcastReceiverModule);

    ActivityComponent newActivityComponent(ActivityModule activityModule);

    ServiceComponent newServiceComponent(ServiceModule serviceModule);
}
