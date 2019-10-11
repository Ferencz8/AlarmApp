package countingsheep.alarm.infrastructure;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.ui.shared.DialogInteractor;
import countingsheep.alarm.util.Constants;

public class ShareHelper implements OnResult {

    public static volatile boolean UserFinishedSharing = false;
    private Activity activity;
    private Runnable reactionRunnable;
    private Handler handler;
    private OnResult onResult;

    public ShareHelper(Activity activity) {
        this.activity = activity;
        UserFinishedSharing = false;
    }


    public void displayShare(String subject, String body, OnResult onResult) {
//
//        final Intent i = new Intent(Intent.ACTION_SEND);
//        i.setType("text/plain");
//        i.putExtra(Intent.EXTRA_TEXT, "text");
//
//        final List<ResolveInfo> activities = activity.getPackageManager().queryIntentActivities(i, 0);
//        final List<ResolveInfo> activitiesDisplayed = new ArrayList<ResolveInfo>();
//        List<String> appNames = new ArrayList<String>();
//        for (ResolveInfo resInfo : activities) {
//            String packageName = resInfo.activityInfo.packageName;
//            Log.i("Package Name", packageName);
//            if (packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("instagram")) {
//                appNames.add(resInfo.loadLabel(activity.getPackageManager()).toString());
//                activitiesDisplayed.add(resInfo);
//            }
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("Complete Action using...");
//        builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                ResolveInfo info = activitiesDisplayed.get(item);
//                if (info.activityInfo.packageName.equals("com.facebook.katana")) {
//                    // Facebook was chosen
//                } else if (info.activityInfo.packageName.equals("com.twitter.android")) {
//                    // Twitter was chosen
//                }
//
//                // start the selected activity
//                i.setPackage(info.activityInfo.packageName);
//                startReactionTimer();
////                activity.startActivityForResult(i, Constants.ShareRequestCode);
//                activity.startActivity(i);
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
//
//        this.onResult = onResult;
//        List<Intent> targetShareIntents = new ArrayList<Intent>();
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        List<ResolveInfo> resInfos = activity.getPackageManager().queryIntentActivities(shareIntent, 0);
//        if (!resInfos.isEmpty()) {
//            System.out.println("Have package");
//            for (ResolveInfo resInfo : resInfos) {
//                String packageName = resInfo.activityInfo.packageName;
//                Log.i("Package Name", packageName);
//                if (packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("instagram")) {
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
//                    intent.setAction(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Text");
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                    intent.setPackage(packageName);
//                    targetShareIntents.add(intent);
//                }
//            }
//            if (!targetShareIntents.isEmpty()) {
//                System.out.println("Have Intent");
//                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
//                chooserIntent.
//                startReactionTimer();
//                activity.startActivityForResult(chooserIntent, Constants.ShareRequestCode);
//            } else {
//                //
//            }
//        }


//
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
//        this.activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void startReactionTimer() {
        handler = new Handler();
        reactionRunnable = () -> {
            try {

                UserFinishedSharing = true;
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        };

        handler.postDelayed(reactionRunnable, 1000 * 5);//5 sec
    }

    public void cancelReactionTimer() {
        handler.removeCallbacks(reactionRunnable);
    }

    @Override
    public void onSuccess(Object result) {
        if (onResult != null) {
            onResult.onSuccess(result);
        }
    }

    @Override
    public void onFailure() {
        if (onResult != null) {
            onResult.onFailure();
        }
    }
}
