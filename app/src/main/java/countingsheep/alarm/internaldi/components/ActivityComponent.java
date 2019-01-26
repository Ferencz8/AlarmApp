package countingsheep.alarm.internaldi.components;

import android.app.Activity;

import com.facebook.core.Core;

import javax.inject.Singleton;

import countingsheep.alarm.MainActivity;
import countingsheep.alarm.activities.AddAlarmActivity;
import countingsheep.alarm.activities.AlarmListActivity;
import countingsheep.alarm.activities.LoginActivity;
import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.CoreModule;
import countingsheep.alarm.internaldi.modules.DataAccessModule;
import dagger.Component;
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