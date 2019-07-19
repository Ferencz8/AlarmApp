package countingsheep.alarm;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import countingsheep.alarm.internaldi.components.ApplicationComponent;
import countingsheep.alarm.internaldi.components.DaggerApplicationComponent;
import countingsheep.alarm.internaldi.modules.ApplicationModule;
import countingsheep.alarm.ui.background.SyncerWorker;

public class AlarmApplication extends Application {

    private static ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();


        startSyncWorkJob();
    }

    private void startSyncWorkJob(){

        final PeriodicWorkRequest periodicSyncWorkRequest = new PeriodicWorkRequest
                .Builder(SyncerWorker.class, 24, TimeUnit.HOURS)
               // .Builder(SyncerWorker.class, 4, TimeUnit.MINUTES)
                .addTag("periodic_sync_work")
                .build();
        WorkManager.getInstance().enqueue(periodicSyncWorkRequest);
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
