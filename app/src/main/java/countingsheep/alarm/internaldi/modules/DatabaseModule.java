package countingsheep.alarm.internaldi.modules;

import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.util.Constants;
import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class DatabaseModule {


    @Provides
    @Singleton
    AlarmDatabase providesAlarmDatabase(AlarmApplication application){
        return Room.databaseBuilder(application, AlarmDatabase.class, Constants.AlarmDbName).build();
    }
}
