package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class UserRegistration {

    @Expose
    private int userId;

    @Expose
    private String customerId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
