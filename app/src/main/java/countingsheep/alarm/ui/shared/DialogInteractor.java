package countingsheep.alarm.ui.shared;

import android.app.TimePickerDialog;

public interface DialogInteractor {

    interface OnClick{
        void doWork();
    }

    interface OnReaction{

        void onPositive();

        void onNegative();
    }

    /**
     * Displays a dialog using the specified parameters with the possibility to react with OK or not.
     *
     * @param imageResourceId
     * @param text
     * @param onClick
     */
    void displayInfoDialog(int imageResourceId, String text, OnClick onClick);

    void displayInfoDialog(int imageResourceId, String text);

    /**
     * Displays an alert dialog using the specified parameters with the possibility to react with OK/Cancel or not.
     *
     * @param title
     * @param message
     * @param onReaction
     */
    void displayReactiveDialog(String title, String message, OnReaction onReaction);

    /**
     * Displays a time picker dialog, with default hour & minutes
     * @param onTimeSetListener
     * @param hour
     * @param minutes
     */
    void displayTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener);

    /**
     * Displays a time picker dialog, with specified hour & minutes
     * @param onTimeSetListener
     * @param hour
     * @param minutes
     */
    void displayTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, int hour, int minutes);
}
