package countingsheep.alarm.internaldi.modules;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.dataaccess.AlarmDatabase;
import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class DatabaseModule {

    @Provides
    @Singleton
    AlarmDatabase providesAlarmDatabase(AlarmApplication application){
        return Room.databaseBuilder(application, AlarmDatabase.class, "db_countingSheep").build();
    }
}
