package countingsheep.alarm.ui.shared;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * DialogInteractorImpl is a class used to display customized dialog's to the user.
 * It was created following: https://code.tutsplus.com/tutorials/showing-material-design-dialogs-in-an-android-app--cms-30013
 */
@Singleton
public class DialogInteractorImpl implements DialogInteractor {


    private Activity activity;

    @Inject
    public DialogInteractorImpl(Activity activity) {
        this.activity = activity;
    }

    /**
     * Displays an alert dialog using the specified parameters with the possibility to react with OK/Cancel or not.
     *
     * @param title
     * @param message
     * @param onReaction
     */
    @Override
    public void displayDialog(String title, String message, final OnReaction onReaction) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.activity)
                .setTitle(title)
                .setMessage(message);

        boolean hasInteraction = onReaction!=null;

        if (hasInteraction) {
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    onReaction.onPositive();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onReaction.onNegative();
                }
            });
        }

        AlertDialog dialog = dialogBuilder.create();

        dialog.setCanceledOnTouchOutside(!hasInteraction); // Does not allow clicks outside the alert dialog

        //dialog.setCancelable(false);//Prevents dismissing the dialog by pressing the BACK button
        dialog.show();
    }


    /**
     * Displays a time picker dialog, with default hour & minutes
     * @param onTimeSetListener
     * @param hour
     * @param minutes
     */
    public void displayTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener){

        int defaultHour = 7;
        int defaultMinutes = 5;

        this.displayTimePickerDialog(onTimeSetListener, defaultHour, defaultMinutes);
    }


    /**
     * Displays a time picker dialog, with specified hour & minutes
     * @param onTimeSetListener
     * @param hour
     * @param minutes
     */
    public void displayTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, int hour, int minutes) {


        //TODO:: add a theme
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.activity, onTimeSetListener, hour, minutes, true);
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.show();
    }
}
