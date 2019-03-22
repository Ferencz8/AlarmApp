package countingsheep.alarm.internaldi.modules;

import android.app.Activity;

import javax.inject.Singleton;

import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.ui.shared.DialogInteractorImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @Singleton
    Activity activity() {
        return this.activity;
    }

    @Provides
    @Singleton
    DialogInteractor providesDialogInteractor(DialogInteractorImpl dialogInteractor){
        return dialogInteractor;
    }
}