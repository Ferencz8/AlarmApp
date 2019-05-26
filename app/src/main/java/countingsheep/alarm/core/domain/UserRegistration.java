package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class UserRegistration {

    @Expose
    private int userId;

    @Expose
    private String customerId;

    @Expose
    private String token;

    @Expose
    private int moneySpentOnSnooze;

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

    public int getMoneySpentOnSnooze() {
        return moneySpentOnSnooze;
    }

    public void setMoneySpentOnSnooze(int moneySpentOnSnooze) {
        this.moneySpentOnSnooze = moneySpentOnSnooze;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
