package countingsheep.alarm.network.httpservices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.SocialAuthenticationService;
import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;
import countingsheep.alarm.core.services.TimeService;

@Singleton
public class FacebookAuthenticationServiceImpl implements SocialAuthenticationService {

    private Activity activity;
    private TimeService timeService;
    private List<OnSocialLoginResult> callbacks;
    private CallbackManager callbackManager;
    private List<String> FacebookPermissions = Arrays.asList("email", "public_profile", "user_location", "user_hometown", "user_gender", "user_birthday");
    private String FacebookUserFields = "id,email,last_name,first_name, picture, location, hometown, gender, birthday";


    @Inject
    public FacebookAuthenticationServiceImpl(Activity activity, TimeService timeService) {
        this.activity = activity;
        this.timeService = timeService;
        this.callbacks = new ArrayList<OnSocialLoginResult>();
    }


    @Override
    public void registerCallback(OnSocialLoginResult onResult) {
        this.callbacks.add(onResult);
    }

    @Override
    public void login() {

        for (final OnSocialLoginResult onResult : this.callbacks) {
            //if there is a stored login result token
            if (AccessToken.getCurrentAccessToken() != null) {
                Log.d("progress", "progress");
                requestEmail(onResult, AccessToken.getCurrentAccessToken());
            } else {//if not, then a login is made and with the result the email details are requested
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().logInWithReadPermissions(activity, FacebookPermissions);

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        requestEmail(onResult, loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        //todo log
                        onResult.onCancel();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //todo log
                        onResult.onError(error);
                    }
                });
            }
        }
    }

    private void requestEmail(final OnSocialLoginResult onResult, AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if(object == null)
                    onResult.onError(new Exception("Facebook GraphRequest Object is null"));

                Log.d("response: ", response.toString());
                try {
                    User loggedInUser = extractUser(object);

                    onResult.onSuccess(loggedInUser);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = initRequestParameters();
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private Bundle initRequestParameters() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", FacebookUserFields);
        return parameters;
    }

    private User extractUser(JSONObject object) throws MalformedURLException, JSONException {
        User user = new User();
//        profilePictureURL = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
//        Log.d("response", profilePictureURL.toString());
        user.firstName = object.optString("first_name", null);
        user.lastName = object.optString("last_name", null);
        user.email = object.optString("email", null);
        user.gender = object.optString("gender", null);
        user.location = object.optString("location", null);
        user.birthday = this.timeService.getDateFromString(object.optString("birthday", null));
        return user;
    }
}
