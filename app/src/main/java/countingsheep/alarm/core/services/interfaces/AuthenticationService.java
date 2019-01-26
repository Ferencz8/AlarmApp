package countingsheep.alarm.core.services.interfaces;

import countingsheep.alarm.core.datainterfaces.OnSocialLoginResult;

public interface AuthenticationService {

    void socialLogin(OnSocialLoginResult onResult);
}
