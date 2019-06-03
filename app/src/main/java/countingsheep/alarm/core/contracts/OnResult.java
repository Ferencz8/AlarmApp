package countingsheep.alarm.core.contracts;

public interface OnResult<T> {

    void onSuccess(T result);

    default void onFailure(){}

    default void onFailure(String message){}
}