package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class SMSScheduledRoast {
    @Expose
    private int senderUserId;
    @Expose
    private int messageId;
    @Expose
    private String recipientPhoneNo;
    @Expose
    private Date dateCreated;
    @Expose
    private Date scheduledDate;

    public int getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(int senderUserId) {
        this.senderUserId = senderUserId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getRecipientPhoneNo() {
        return recipientPhoneNo;
    }

    public void setRecipientPhoneNo(String recipientPhoneNo) {
        this.recipientPhoneNo = recipientPhoneNo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
