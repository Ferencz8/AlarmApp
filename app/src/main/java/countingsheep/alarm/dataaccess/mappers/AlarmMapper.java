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
        alarm.id = entity.getId();
        alarm.isTurnedOn = entity.isTurnedOn();
        alarm.isVobrateOn = entity.isVobrateOn();
        alarm.minutes = entity.getMinutes();
        alarm.seconds = entity.getSeconds();
        alarm.title = entity.getTitle();
        alarm.volume = entity.getVolume();

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
        alarm.setTitle(model.title);
        alarm.setId(model.id);
        alarm.setTurnedOn(model.isTurnedOn);
        alarm.setVobrateOn(model.isVobrateOn);
        alarm.setMinutes(model.minutes);
        alarm.setSeconds(model.seconds);
        alarm.setVolume(model.volume);

        return alarm;
    }
}
