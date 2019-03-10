package countingsheep.alarm.core.contracts.infrastructure;

public interface EMailService {

    void SendEMail(String recipient, String subject, String body);

    void SendReportAProblemEMail(String body);

    void SendFeedbackEMail(String body);
}
