package countingsheep.alarm.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import countingsheep.alarm.db.entities.Monetization;

@Dao
public interface MonetizationDao extends BaseDao<Monetization> {

    @Query("SELECT * FROM Monetization")
    List<Monetization> getAll();

    @Query("SELECT * FROM Monetization WHERE id =:monetizationId")
    Monetization getById(int monetizationId);
}
