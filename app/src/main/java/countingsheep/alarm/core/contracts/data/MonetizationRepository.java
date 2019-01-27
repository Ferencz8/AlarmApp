package countingsheep.alarm.core.contracts.data;

import java.util.List;

import countingsheep.alarm.dataaccess.entities.Monetization;

public interface MonetizationRepository {

    void insert(Monetization monetization);

    void update(Monetization monetization);

    Monetization get(int id);

    List<Monetization> get();
}
