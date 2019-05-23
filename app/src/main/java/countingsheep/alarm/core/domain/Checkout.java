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
    private int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
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
