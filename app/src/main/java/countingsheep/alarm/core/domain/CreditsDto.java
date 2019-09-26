package countingsheep.alarm.core.domain;

public class CreditsDto {

    private int credits;

    private boolean isEternal;

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public boolean isEternal() {
        return isEternal;
    }

    public void setEternal(boolean eternal) {
        isEternal = eternal;
    }
}
