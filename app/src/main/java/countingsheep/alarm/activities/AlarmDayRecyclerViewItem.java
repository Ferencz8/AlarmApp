package countingsheep.alarm.activities;

public class AlarmDayRecyclerViewItem {

    private String text;
    private int textColor;
    private int imageResourceId;

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
