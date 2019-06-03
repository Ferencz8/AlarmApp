package countingsheep.alarm.network.tasks;

import android.net.Uri;

import androidx.annotation.NonNull;

public interface ProfilePictureTaskResponse {
    void OnImageAvailable(@NonNull String imagePath);
}
