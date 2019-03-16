package countingsheep.alarm.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

@Dao
public interface BaseDao<T> {

    @Insert
    long insert (T entity);

    @Update
    void update(T entity);

    @Delete
    void delete(T entity);
}
