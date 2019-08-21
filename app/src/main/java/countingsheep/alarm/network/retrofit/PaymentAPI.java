package countingsheep.alarm.network.retrofit;

import countingsheep.alarm.core.domain.Checkout;
import countingsheep.alarm.core.domain.Checkout2;
import countingsheep.alarm.core.domain.Customer;
import countingsheep.alarm.core.domain.CustomerRegistration;
import countingsheep.alarm.core.domain.PaymentRegistration;
import countingsheep.alarm.db.entities.PaymentStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentAPI {

    /*
    * Makes a request to the server to generate a new token.
    * If customerId is null then the token which will be provided will help create a new customer account
    * It it is not null, then it will display the vault of the linked customer
    * */
    //@GET("/alarm/api/payments/generatetoken")
    @Headers({"Accept: application/json"})
    @GET("/alarm/api/payment/generatetoken")
    Call<String> generateToken(@Query("customerId") String customerId);

    @POST("/alarm/api/payment/Checkout")
    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> checkout(@Body Checkout checkout);

    @POST("/alarm/api/payment/ProcessSnooze")
        //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> checkout2(@Body Checkout2 checkout);


    @POST("/alarm/api/payment/Register")
    @Headers({"Accept: application/json"})
    Call<Customer> register(@Body CustomerRegistration customerRegistration);


    @POST("/alarm/api/payment/addOrUpdatePaymentMethod")
    @Headers({"Accept: application/json"})
    Call<String> addOrUpdatePaymentMethod(@Body PaymentRegistration paymentRegistration);

    @Headers({"Accept: application/json"})
    @GET("/alarm/api/payment/GetPaymentStatus")
    Call<PaymentStatus> getPaymentStatus(@Query("transactionId") String transactionId);
}
