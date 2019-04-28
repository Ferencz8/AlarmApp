package countingsheep.alarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import countingsheep.alarm.internaldi.components.ActivityComponent;
import countingsheep.alarm.internaldi.components.BroadcastReceiverComponent;
import countingsheep.alarm.internaldi.components.ServiceComponent;
import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.BroadcastReceiverModule;
import countingsheep.alarm.internaldi.modules.ServiceModule;

public class Injector {

    private static ActivityComponent activityComponent;
    private static BroadcastReceiverComponent broadcastReceiverComponent;
    private static ServiceComponent serviceComponent;


    public static ActivityComponent getActivityComponent(Activity activity){
        activityComponent = AlarmApplication.getApplicationComponent()
                .newActivityComponent(new ActivityModule(activity));
        return activityComponent;
    }

    public static BroadcastReceiverComponent getBroadcastReceiverComponent(Context context){
        broadcastReceiverComponent = AlarmApplication.getApplicationComponent()
                .newBroadcastReceiverComponent(new BroadcastReceiverModule(context));
        return broadcastReceiverComponent;
    }

    public static ServiceComponent getServiceComponent(Context context){
        serviceComponent = AlarmApplication.getApplicationComponent()
                .newServiceComponent(new ServiceModule(context));
        return serviceComponent;
    }
}
