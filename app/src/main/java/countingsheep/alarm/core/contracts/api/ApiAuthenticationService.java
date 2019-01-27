package countingsheep.alarm.core.contracts.api;

import countingsheep.alarm.core.domain.User;

public interface ApiAuthenticationService {

    void register(User user);
}
