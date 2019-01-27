package countingsheep.alarm.core.domain;

import java.io.Serializable;

public class AlarmModel implements Serializable {

    private int id;

    private int hours;

    private int minutes;

    private boolean isTurnedOn;

    private String title;

    private boolean isVobrateOn;

    private int volume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean isTurnedOn() {
        return isTurnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        isTurnedOn = turnedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVobrateOn() {
        return isVobrateOn;
    }

    public void setVobrateOn(boolean vobrateOn) {
        isVobrateOn = vobrateOn;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
