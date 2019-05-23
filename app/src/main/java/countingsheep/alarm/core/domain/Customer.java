package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class Customer {

    @Expose
    private String customerId;

    @Expose
    private String token;

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
}
