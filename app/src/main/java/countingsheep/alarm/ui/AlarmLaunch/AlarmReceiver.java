package countingsheep.alarm.ui.AlarmLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("In receiver", "Yay!");

        // Fetch extra strings from MainActivity on button intent
        // Determines whether user presses on or off
        String fetch_string = intent.getExtras().getString("extra");
        // Log.e("What is the key?", fetch_string);

        // Fetch extra longs from MainActivity intent
        // Tells which value user selects from spinner
        int get_sound_choice = intent.getExtras().getInt("sound_choice");
        //Log.e("Sound choice is ", get_sound_choice.toString());

        // Create intent
        Intent alarmintent = new Intent(context, AlarmLaunchActivity.class);
        alarmintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Pass extra string from receiver to RingtonePlayingService
        alarmintent.putExtra("extra", fetch_string);

        // Pass extra integer from receiver to RingtonePlayingService
        alarmintent.putExtra("sound_choice", get_sound_choice);

        // Start ringtone service
        context.startActivity(alarmintent);
    }
}
