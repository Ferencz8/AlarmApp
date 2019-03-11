package countingsheep.alarm.ui.shared;

import android.app.TimePickerDialog;

public interface DialogInteractor {

    interface OnReaction{

        void onPositive();

        void onNegative();
    }

    void displayDialog(String title, String message, OnReaction onReaction);

    void displayTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener);
}
