package countingsheep.alarm.internaldi.components;

import android.content.BroadcastReceiver;

import javax.inject.Singleton;

import countingsheep.alarm.MainActivity;
import countingsheep.alarm.internaldi.modules.InfrastructureModule;
import countingsheep.alarm.internaldi.modules.NetworkModule;
import countingsheep.alarm.ui.OnBoardingActivity;
import countingsheep.alarm.ui.addEditAlarm.AddAlarmActivity;
import countingsheep.alarm.ui.alarmLaunch.AlarmLaunchActivity;
import countingsheep.alarm.ui.alarmList.AlarmsFragment;
import countingsheep.alarm.ui.LoginActivity;
import countingsheep.alarm.internaldi.modules.ActivityModule;
import countingsheep.alarm.internaldi.modules.CoreModule;
import countingsheep.alarm.internaldi.modules.DataAccessModule;
//import countingsheep.alarm.ui.card.CardActivity;
import countingsheep.alarm.ui.freecredits.FreeCreditsActivity;
import dagger.Subcomponent;


@Singleton
@Subcomponent(modules = {
        ActivityModule.class,
        CoreModule.class,
        NetworkModule.class,
        DataAccessModule.class,
        InfrastructureModule.class
})
public interface ActivityComponent {

    void inject(AddAlarmActivity addAlarmActivity);

    void inject(LoginActivity loginActivity);

    void inject(AlarmsFragment alarmFragment);

    void inject(MainActivity mainActivity);

    void inject(AlarmLaunchActivity alarmLaunchActivity);

    void inject(OnBoardingActivity onBoardingActivity);

    void inject(FreeCreditsActivity freeCreditsActivity);

    //void inject(CardActivity cardActivity);
}