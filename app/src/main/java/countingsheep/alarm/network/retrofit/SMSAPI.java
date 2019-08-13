package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.SMSRoastMessageReq;
import countingsheep.alarm.core.domain.SMSScheduledRoast;
import countingsheep.alarm.core.domain.SavePhoneNoReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SMSAPI {
    @POST("/alarm/api/sms/send")
    Call<String> sendRoastSMS(@Body SMSRoastMessageReq roastMessageReq);

    @POST("/alarm/api/sms/SendToFriend")
    Call<String> sendRoastSMSToFriend(@Body SMSScheduledRoast smsScheduledRoast);
}
