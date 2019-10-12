package countingsheep.alarm.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class DeviceUtil {

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
            );
        } catch (Exception ignored) {
        }
    }
}
