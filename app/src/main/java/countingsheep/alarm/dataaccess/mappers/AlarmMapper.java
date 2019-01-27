package countingsheep.alarm.dataaccess.mappers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import countingsheep.alarm.core.domain.AlarmModel;
import countingsheep.alarm.dataaccess.entities.Alarm;

public class AlarmMapper {


    @Inject
    public AlarmMapper() {
    }

    public AlarmModel mapToAlarmModel(Alarm entity) {
        AlarmModel alarm = new AlarmModel();
        alarm.setId(entity.getId());
        alarm.setTurnedOn(entity.isTurnedOn());
        alarm.setVobrateOn(entity.isVobrateOn());
        alarm.setHours(entity.getHours());
        alarm.setMinutes(entity.getMinutes());
        alarm.setTitle(entity.getTitle());
        alarm.setVolume(entity.getVolume());

        return alarm;
    }

    public List<AlarmModel> mapToAlarmModel(List<Alarm> entities) {
        ArrayList<AlarmModel> alarms = new ArrayList<>();

        for (Alarm entity: entities) {
            alarms.add(mapToAlarmModel(entity));
        }
        return alarms;
    }


    public Alarm mapToAlarmDb(AlarmModel model) {
        Alarm alarm = new Alarm();
        alarm.setTitle(model.getTitle());
        alarm.setId(model.getId());
        alarm.setTurnedOn(model.isTurnedOn());
        alarm.setVobrateOn(model.isVobrateOn());
        alarm.setMinutes(model.getMinutes());
        alarm.setHours(model.getHours());
        alarm.setVolume(model.getVolume());

        return alarm;
    }
}
