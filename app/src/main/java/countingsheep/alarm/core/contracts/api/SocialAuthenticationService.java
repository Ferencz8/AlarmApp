package countingsheep.alarm.core.contracts.api;

import android.content.Intent;

public interface SocialAuthenticationService {

    /*  This method is used to register callbacks, which get called once the login operation is completed. */
    void registerCallback(OnSocialLoginResult onResult);

    /* Performs the login operation and then notifies all observers/callbacks*/
    void login();

    void logout();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean isUserLoggedIn();
}
