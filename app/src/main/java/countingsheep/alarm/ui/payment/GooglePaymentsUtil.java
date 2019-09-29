package countingsheep.alarm.ui.payment;

import android.app.Activity;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import countingsheep.alarm.R;

import static com.google.android.gms.wallet.WalletConstants.ENVIRONMENT_TEST;

public class GooglePaymentsUtil {

    //<-- PAYMENT TRANSACTION
    public static JSONObject getPaymentDataRequest(String price) {
        try {
            JSONObject paymentDataRequest = getBaseRequest();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", getMerchantInfo());

            paymentDataRequest.put("shippingAddressRequired", false); // TODO set shipping address required

            JSONObject shippingAddressParameters = new JSONObject();
            shippingAddressParameters.put("phoneNumberRequired", false);  // TODO set phone number required

            //JSONArray allowedCountryCodes = new JSONArray(); // TODO set shipping supported countries if it's enabled
            //shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);

            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);

            return paymentDataRequest;
        } catch (JSONException e) {
            return null;
        }
    }

    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("currencyCode", "USD"); // TODO set currency

        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        //return new JSONObject().put("merchantName", "Example Merchant");

        return new JSONObject()
                .put("merchantName", "DarkSheep") // TODO check info and set for PROD
                .put("merchantId", "04066343836641766162");
    }
    //--------> END PAYMENT TRANSACTION



    //<-- CONFIGURE PAYMENT
    // TODO change environment to production
    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(ENVIRONMENT_TEST).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    // Check if the user can pay through Google Pay
    public static JSONObject getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));

            return isReadyToPayRequest;
        } catch (JSONException e) {
            return null;
        }
    }

    // Setting Google Pay API version
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    // Configure merchant
    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    // Setting all configurations
    // !! Paypal is also available as card payment method
    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
        parameters.put("allowedCardNetworks", getAllowedCardNetworks());

        JSONObject billingAddressParameters = new JSONObject();
        billingAddressParameters.put("format", "FULL");

        parameters.put("billingAddressParameters", billingAddressParameters);

        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    // TODO check supported security with payment provider
    // Configure card auth methods like 3D Secure
    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS");
    }

    // TODO check supported cards with payment provider
    // Configure card providers
    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("INTERAC")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA");
    }

    // Configure merchant
    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject(){{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject(){{
                put("gateway", "example"); // TODO change with real gateway
                put("gatewayMerchantId", "exampleGatewayMerchantId"); // TODO change with real gateway merchant id
            }
            });
        }};
    }
    //--------> END PAYMENT CONFIG
}