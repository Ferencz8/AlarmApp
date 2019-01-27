package countingsheep.alarm.dataaccess.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Alarm;

@Dao
public interface AlarmDao extends BaseDao<Alarm>{

    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarm WHERE id =:alarmId")
    Alarm getById(int alarmId);
}
