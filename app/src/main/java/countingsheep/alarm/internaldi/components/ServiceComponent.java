package countingsheep.alarm.internaldi.components;

import javax.inject.Singleton;

import countingsheep.alarm.internaldi.modules.BroadcastReceiverModule;
import countingsheep.alarm.internaldi.modules.CoreModule;
import countingsheep.alarm.internaldi.modules.DataAccessModule;
import countingsheep.alarm.internaldi.modules.ServiceModule;
//import countingsheep.alarm.ui.alarmLaunch.AlarmBootService;
import countingsheep.alarm.ui.background.SyncerWorker;
import countingsheep.alarm.ui.foreground.ProcessFailedPaymentsService;
import dagger.Subcomponent;

@Singleton
@Subcomponent(
        modules = {
                ServiceModule.class,
                CoreModule.class,
                DataAccessModule.class
        })
public interface ServiceComponent {

    //void inject(AlarmBootService alarmBootService);

    void inject(SyncerWorker syncerWorker);

    void inject(ProcessFailedPaymentsService processFailedPaymentsService);
}
