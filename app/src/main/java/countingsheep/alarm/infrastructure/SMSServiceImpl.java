package countingsheep.alarm.infrastructure;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.infrastructure.SMSService;

@Singleton
public class SMSServiceImpl implements SMSService {

    private static final String SentAction = "SMS_SENT";
    private static final String DeliveredAction = "SMS_DELIVERED";
    private Context context;

    @Inject
    public SMSServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void sendSMS(String textMessage, String phoneNumber) {
        try {

            this.context.registerReceiver(getSentStatusReceiver(), new IntentFilter(SentAction));
            this.context.registerReceiver(getDeliveredStatusReceiver(), new IntentFilter(DeliveredAction));

            SmsManager sms = SmsManager.getDefault();
            // if message length is too long messages are divided
            List<String> messages = sms.divideMessage(textMessage);
            for (String msg : messages) {

                PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SentAction), 0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(DeliveredAction), 0);
                sms.sendTextMessage(phoneNumber, null, msg, sentIntent, deliveredIntent);
            }
        }
        catch(Exception exception){
            //TODO: log
        }
    }

    private void requestPermissions(){

    }

    private BroadcastReceiver getDeliveredStatusReceiver(){
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Message Not Delivered";
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        };
    }

    private BroadcastReceiver getSentStatusReceiver(){
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error : Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio is off";
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
