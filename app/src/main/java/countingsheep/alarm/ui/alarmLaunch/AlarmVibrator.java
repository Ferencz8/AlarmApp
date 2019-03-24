package countingsheep.alarm.ui.alarmLaunch;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Vibrator;

/**
 * This simple utility class is a wrapper of the system vibrator.
 */
public class AlarmVibrator {
    private boolean mVibrating;
    private Vibrator mVibrator;
    private Context mContext;

    public AlarmVibrator(Context context) {
        mContext = context;
        initialize();
    }

    public void initialize() {
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void cleanup() {
        mVibrator = null;
    }

    public void vibrate() {
        if (!mVibrating) {
            // Start immediately
            // Vibrate for 500 milliseconds
            // Sleep for 500 milliseconds
            long[] vibrationPattern = {0, 500, 500};
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mVibrator.vibrate(vibrationPattern, 0,
                        new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            } else {
                mVibrator.vibrate(vibrationPattern, 0);
            }
            mVibrating = true;
        }
    }

    public void stop() {
        if (mVibrating) {
            mVibrator.cancel();
            mVibrating = false;
        }
    }
}
