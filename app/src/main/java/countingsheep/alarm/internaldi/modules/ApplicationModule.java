package countingsheep.alarm.internaldi.modules;

import android.content.Context;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
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
}
