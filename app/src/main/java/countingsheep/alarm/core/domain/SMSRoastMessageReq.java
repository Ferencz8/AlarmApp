package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class SMSRoastMessageReq {
    @Expose
    private int userId;
    @Expose
    private String phoneNo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNo;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNo = phoneNumber;
    }
}
