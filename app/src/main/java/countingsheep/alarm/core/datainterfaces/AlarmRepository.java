package countingsheep.alarm.core.datainterfaces;

import java.util.List;


import countingsheep.alarm.core.domain.AlarmModel;

public interface AlarmRepository {

    void insert(AlarmModel alarm);

    void update(AlarmModel alarm);

    AlarmModel get(int id);

    List<AlarmModel> get();
}
