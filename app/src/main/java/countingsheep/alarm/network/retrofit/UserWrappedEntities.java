package countingsheep.alarm.network.retrofit;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserWrappedEntities <T> {

    @Expose
    int userId;

    @Expose
    List<T> entities;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
}