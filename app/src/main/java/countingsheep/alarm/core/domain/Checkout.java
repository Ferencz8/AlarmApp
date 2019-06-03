package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class Checkout {

    @Expose
    private String token;

    @Expose
    private int price;

    @Expose
    private int alarmReactionId;

    @Expose
    private String customerId;

    @Expose
    private int userId;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAlarmReactionId() {
        return alarmReactionId;
    }

    public void setAlarmReactionId(int alarmReactionId) {
        this.alarmReactionId = alarmReactionId;
    }
}
