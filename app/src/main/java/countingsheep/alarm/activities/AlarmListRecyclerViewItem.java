package countingsheep.alarm.activities;

public class AlarmListRecyclerViewItem {

    private String title;
    private String hour;
    private String repeatDays;
    private int onOffimageResourceId;
    private int offBackgroundResourceId;

    public int getOffBackgroundResourceId() {
        return offBackgroundResourceId;
    }

    public void setOffBackgroundResourceId(int offBackgroundResourceId) {
        this.offBackgroundResourceId = offBackgroundResourceId;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public int getOnOffimageResourceId() {
        return onOffimageResourceId;
    }

    public void setOnOffimageResourceId(int onOffimageResourceId) {
        this.onOffimageResourceId = onOffimageResourceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
