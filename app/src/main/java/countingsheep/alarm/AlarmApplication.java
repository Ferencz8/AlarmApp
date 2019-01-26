package countingsheep.alarm;

import android.app.Application;

import countingsheep.alarm.internaldi.components.ApplicationComponent;
import countingsheep.alarm.internaldi.components.DaggerApplicationComponent;
import countingsheep.alarm.internaldi.modules.ApplicationModule;

public class AlarmApplication extends Application {

    private static ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        //this.applicationComponent.inject(this);
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {

        return applicationComponent;
    }

}
