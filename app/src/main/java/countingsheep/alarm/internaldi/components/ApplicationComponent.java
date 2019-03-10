package countingsheep.alarm.internaldi.components;


import javax.inject.Singleton;

import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.ApplicationModule;
import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    ActivityComponent newActivityComponent(ActivityModule activityModule);
}
