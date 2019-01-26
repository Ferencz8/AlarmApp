package countingsheep.alarm.internaldi.components;


import android.content.Context;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.dataaccess.entities.Alarm;
import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.ApplicationModule;
import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //void inject(AlarmApplication alarmApplication);

    ActivityComponent newActivityComponent(ActivityModule activityModule);
    //Exposed to sub-graphs.
    //AlarmApplication alarmApplication();
}
