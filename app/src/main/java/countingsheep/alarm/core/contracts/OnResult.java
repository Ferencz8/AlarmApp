package countingsheep.alarm.core.contracts;

public interface OnResult<T> {

    void onSuccess(T result);

    void onFailure();
}