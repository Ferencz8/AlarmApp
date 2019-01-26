package countingsheep.alarm.dataaccess.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Alarm;

@Dao
public interface AlarmDao {

    @Insert
    void insert (Alarm alarm);

    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarm WHERE id =:alarmId")
    Alarm getById(int alarmId);

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);
}
