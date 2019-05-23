package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class CustomerRegistration {

    @Expose
    private int userId;

    @Expose
    private String paymentMethodNonce;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPaymentMethodNonce() {
        return paymentMethodNonce;
    }

    public void setPaymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
    }
}
