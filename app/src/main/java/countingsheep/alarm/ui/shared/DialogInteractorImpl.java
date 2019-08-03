package countingsheep.alarm.ui.shared;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import countingsheep.alarm.R;

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

    public void displayInfoDialog(int imageResourceId, String text){
        this.displayInfoDialog(imageResourceId, text, null);
    }

    public void displayInfoDialog(int imageResourceId, String text, OnClick onClick){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.generic_popup);
        ImageView popupImage = dialog.findViewById(R.id.popupImageView);
        popupImage.setImageResource(imageResourceId);
        TextView popupText = dialog.findViewById(R.id.popupTextId);
        popupText.setText(text);

        TextView okText = dialog.findViewById(R.id.ok_text);
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(onClick!=null){
                    onClick.doWork();
                }
            }
        });
        dialog.show();
    }


    /**
     * Displays an alert dialog using the specified parameters with the possibility to react with OK/Cancel or not.
     *
     * @param title
     * @param message
     * @param onReaction
     */
    @Override
    public void displayReactiveDialog(String title, String message, final OnReaction onReaction) {
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

//        if(dialog.getWindow()!=null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
//        }
        dialog.setCanceledOnTouchOutside(!hasInteraction); // Does not allow clicks outside the alert dialog

        //dialog.setCancelable(false);//Prevents dismissing the dialog by pressing the BACK button
        dialog.show();
    }


    /**
     * Displays a time picker dialog, with default hour & minutes
     * @param onTimeSetListener
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
