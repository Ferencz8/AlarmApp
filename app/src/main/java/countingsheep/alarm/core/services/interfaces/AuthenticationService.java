package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.contracts.api.OnSocialLoginResult;

public interface AuthenticationService {

    void socialLogin(OnSocialLoginResult onResult);

    void socialLogout();
}
