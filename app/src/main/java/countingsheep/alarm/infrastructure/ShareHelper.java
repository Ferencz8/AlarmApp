package countingsheep.alarm.infrastructure;

import android.app.Activity;
import android.content.Intent;

public class ShareHelper {

    private Activity activity;

    public ShareHelper(Activity activity) {
        this.activity = activity;
    }

    public void displayShare(String subject, String body) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        this.activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
