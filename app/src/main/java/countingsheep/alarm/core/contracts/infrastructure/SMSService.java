package countingsheep.alarm.core.contracts.infrastructure;

public interface SMSService {

    void sendSMS(String textMessage, String phoneNumber);
}
