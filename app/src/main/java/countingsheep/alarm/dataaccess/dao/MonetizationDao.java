package countingsheep.alarm.dataaccess.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Monetization;

@Dao
public interface MonetizationDao extends BaseDao<Monetization> {

    @Query("SELECT * FROM Monetization")
    List<Monetization> getAll();

    @Query("SELECT * FROM Monetization WHERE id =:monetizationId")
    Monetization getById(int monetizationId);
}
