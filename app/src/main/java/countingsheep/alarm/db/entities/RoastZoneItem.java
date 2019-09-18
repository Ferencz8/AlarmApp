package countingsheep.alarm.db.entities;

public class RoastZoneItem {
    private String title;
    private String message;
    private boolean isLocked;

    public static final int OPT_YOUR_ROASTS = 0;
    public static final int OPT_ROAST_FRIEND = 1;
    public static final int OPT_LEADERBOARD = 2;
    public static final int OPT_ROAST_CHAT = 3;
    public static final int OPT_BATTLE_FIELD = 4;

    public RoastZoneItem(String title, String message, boolean isLocked) {
        this.title = title;
        this.message = message;
        this.isLocked = isLocked;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
