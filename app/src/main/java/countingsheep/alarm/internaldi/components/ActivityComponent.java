package countingsheep.alarm.internaldi.components;

import javax.inject.Singleton;

import countingsheep.alarm.MainActivity;
import countingsheep.alarm.ui.AddEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.AlarmList.AlarmListActivity;
import countingsheep.alarm.ui.LoginActivity;
import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.CoreModule;
import countingsheep.alarm.internaldi.modules.DataAccessModule;
import dagger.Subcomponent;


@Singleton
@Subcomponent(modules = {
        ActivityModule.class,
        CoreModule.class,
        DataAccessModule.class
})
public interface ActivityComponent {
    //Exposed to sub-graphs.
    //Activity activity();
    //void inject(Activity activity);

    //CoreComponent newCoreComponent(CoreModule coreModule);

    void inject(AddAlarmActivity addAlarmActivity);

    void inject(LoginActivity loginActivity);

    void inject(AlarmListActivity alarmListActivity);

    void inject(MainActivity mainActivity);
}