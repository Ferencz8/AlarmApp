package countingsheep.alarm.db.entities;

public class CreditsPackage {
    private String title;
    private int credits;
    private boolean isEndless;
    private String description;
    private int cost;

    public CreditsPackage(String title, int credits, boolean isEndless, String description, int cost) {
        this.title = title;
        this.credits = credits;
        this.isEndless = isEndless;
        this.description = description;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public boolean isEndless() {
        return isEndless;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }
}
