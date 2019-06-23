package countingsheep.alarm.infrastructure;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.infrastructure.EMailService;

@Singleton
public class EMailServiceImpl implements EMailService {

    private Activity activity;


    private static final String CountingSheepRecipient = "ferenczv8@gmail.com";
    private static final String ReportAProblemSubject = "Report a Problem";
    private static final String FeedbackSubject = "Feedback";
    private static final String ActionSendType = "message/rfc822";
    private static final String GMailPackageName = "com.google.android.gm";

    @Inject
    public EMailServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void SendEMail(String recipient, String subject, String body) {
        Intent mailClient = new Intent(Intent.ACTION_SEND);
        mailClient.setType(ActionSendType);
        mailClient.setPackage(GMailPackageName);
        mailClient.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        mailClient.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailClient.putExtra(Intent.EXTRA_TEXT, body);
        try {
            this.activity.startActivity(Intent.createChooser(mailClient, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Crashlytics.logException(ex);
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void SendReportAProblemEMail(String body) {
        SendEMail(CountingSheepRecipient, ReportAProblemSubject, body);
    }

    @Override
    public void SendFeedbackEMail(String body) {
        SendEMail(CountingSheepRecipient, FeedbackSubject, body);
    }
}
