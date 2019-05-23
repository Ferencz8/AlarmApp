package countingsheep.alarm.core.domain;

import com.google.gson.annotations.Expose;

public class PaymentRegistration {
    @Expose
    private int userId;

    @Expose
    private String paymentMethodNonce;

    @Expose
    private String customerId;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
