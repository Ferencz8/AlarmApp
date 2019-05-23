package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {

    @Insert
    long insert (T entity);

    @Update
    void update(T entity);

    @Delete
    void delete(T entity);
}
