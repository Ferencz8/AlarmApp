package countingsheep.alarm.internaldi.modules;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.util.Constants;
import dagger.Module;
import dagger.Provides;

import static countingsheep.alarm.db.AlarmDatabaseMigration.MIGRATION_1_2;

@Module(includes = ApplicationModule.class)
public class DatabaseModule {


    @Provides
    @Singleton
    AlarmDatabase providesAlarmDatabase(final AlarmApplication application){
        final AlarmDatabase database = Room.databaseBuilder(application, AlarmDatabase.class, Constants.AlarmDbName)
                //.addMigrations(MIGRATION_1_2))
//                .addCallback(new RoomDatabase.Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                database.repeatDayDao().
//                            }
//                        });
//                    }
//                })
                .build();
        return database;
    }
}
