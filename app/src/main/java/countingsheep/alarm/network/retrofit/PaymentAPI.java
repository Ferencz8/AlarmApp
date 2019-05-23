package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.Checkout;
import countingsheep.alarm.core.domain.Customer;
import countingsheep.alarm.core.domain.CustomerRegistration;
import countingsheep.alarm.core.domain.PaymentRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentAPI {

    //@GET("/alarm/api/payments/generatetoken")
    @Headers({"Accept: application/json"})
    @GET("/alarm/api/payment/generatetoken")
    Call<String> generateToken(@Query("customerId") String customerId);

    @POST("/alarm/api/payment/Checkout")
    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> checkout(@Body Checkout checkout);


    @POST("/alarm/api/payment/Register")
    @Headers({"Accept: application/json"})
    Call<Customer> register(@Body CustomerRegistration customerRegistration);


    @POST("/alarm/api/payment/addOrUpdatePaymentMethod")
    @Headers({"Accept: application/json"})
    Call<String> addOrUpdatePaymentMethod(@Body PaymentRegistration paymentRegistration);
}
