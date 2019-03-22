package countingsheep.alarm.core.contracts.data;

public interface OnAsyncResponse<T> {

    void processResponse(T response);
}
