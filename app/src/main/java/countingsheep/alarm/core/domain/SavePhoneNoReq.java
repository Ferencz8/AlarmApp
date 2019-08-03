package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class SavePhoneNoReq {

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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
