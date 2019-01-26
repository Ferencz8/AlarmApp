package countingsheep.alarm;

import android.app.Activity;

import javax.inject.Inject;

import countingsheep.alarm.internaldi.components.ActivityComponent;
import countingsheep.alarm.internaldi.modules.ActivityModule;

public class Injector {

    private static ActivityComponent activityComponent;


    public static ActivityComponent getActivityComponent(Activity activity){
        activityComponent = AlarmApplication.getApplicationComponent()
                .newActivityComponent(new ActivityModule(activity));
        return activityComponent;
    }
}
