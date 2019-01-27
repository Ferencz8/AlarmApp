package countingsheep.alarm.internaldi.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.util.Constants;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {


    private final AlarmApplication application;

    public ApplicationModule(AlarmApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    AlarmApplication provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(AlarmApplication application) {
        return application.getSharedPreferences(Constants.PrefFileName,Context.MODE_PRIVATE);
    }
}
