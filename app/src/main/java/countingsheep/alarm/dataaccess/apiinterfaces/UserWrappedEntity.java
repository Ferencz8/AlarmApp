package countingsheep.alarm.dataaccess.apiinterfaces;

import com.google.gson.annotations.Expose;

public class UserWrappedEntity<T> {

    @Expose
    int userId;

    @Expose
    T entity;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
