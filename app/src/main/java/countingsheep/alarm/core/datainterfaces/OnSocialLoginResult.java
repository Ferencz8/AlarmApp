package countingsheep.alarm.core.datainterfaces;
import countingsheep.alarm.core.domain.User;

public interface OnSocialLoginResult {

    void onSuccess(User user);

    void onCancel();

    void onError(Exception exception);
}
