package countingsheep.alarm.internaldi.modules;

import android.content.BroadcastReceiver;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BroadcastReceiverModule {
    private final Context context;

    public BroadcastReceiverModule(Context context) {
        this.context = context;
    }

    /**
     * Expose the context to dependents in the graph.
     */
    @Provides
    @Singleton
    Context context() {
        return this.context;
    }

}
