package countingsheep.alarm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import countingsheep.alarm.util.Constants;

public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity activity;
    private static boolean customExceptionHandlerAttached = false;
    protected FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.LOG_OUT));

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if(!customExceptionHandlerAttached) {
            //https://stackoverflow.com/questions/4427515/using-global-exception-handling-on-android
            final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    //Do your own error handling here

                    Crashlytics.setString("GlobalException", "GlobalExceptionHandler");
                    Crashlytics.logException(paramThrowable);
                    if (oldHandler != null)
                        oldHandler.uncaughtException(paramThread, paramThrowable); //Delegates to Android's error handling
                    else
                        System.exit(2); //Prevents the service/app from freezing
                }
            });

            customExceptionHandlerAttached = true;
        }
    }

    // handler for received Intents for logout event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //do your code snippet here.
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("BaseActivity", "I am in base activity");

        super.onActivityResult(requestCode, resultCode, data);
    }
}
