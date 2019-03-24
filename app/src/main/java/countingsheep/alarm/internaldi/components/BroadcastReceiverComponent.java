package countingsheep.alarm.internaldi.components;

import javax.inject.Singleton;

import countingsheep.alarm.internaldi.modules.BroadcastReceiverModule;
import countingsheep.alarm.internaldi.modules.CoreModule;
import countingsheep.alarm.internaldi.modules.DataAccessModule;
import countingsheep.alarm.ui.alarmLaunch.AlarmReceiver;
import dagger.Subcomponent;


@Singleton
@Subcomponent(
        modules = {
        BroadcastReceiverModule.class,
        CoreModule.class,
        DataAccessModule.class
})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);
}
