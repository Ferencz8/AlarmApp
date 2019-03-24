package countingsheep.alarm.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;

public class AlarmDatabaseMigration {

    /**
     * This acts as an example of how to migrate a version of db schema.
     * This is needed to be run since the initial schema of the db differs from the 1st one.
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //database.execSQL("ALTER TABLE alarm ADD COLUMN ringtone TEXT");
        }
    };
}
