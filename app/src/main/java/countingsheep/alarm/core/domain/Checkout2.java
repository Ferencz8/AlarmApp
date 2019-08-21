package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

import countingsheep.alarm.db.entities.Alarm;
import countingsheep.alarm.db.entities.AlarmReaction;

public class Checkout2 {

    @Expose
    private String token;

    @Expose
    private int price;

    @Expose
    private AlarmReaction alarmReaction;

    @Expose
    private Alarm alarm;

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

    public AlarmReaction getAlarmReaction() {
        return alarmReaction;
    }

    public void setAlarmReaction(AlarmReaction alarmReaction) {
        this.alarmReaction = alarmReaction;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}
