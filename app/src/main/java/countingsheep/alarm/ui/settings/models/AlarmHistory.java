package countingsheep.alarm.ui.settings.models;

public class AlarmHistory {

    private String name;
    private String createdDate;
    private Integer cashSpent;
    private String ringingTime;
    private String reactionType;
    private boolean requireRefund;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCashSpent() {
        return cashSpent;
    }

    public void setCashSpent(Integer cashSpent) {
        this.cashSpent = cashSpent;
    }

    public String getRingingTime() {
        return ringingTime;
    }

    public void setRingingTime(String ringingTime) {
        this.ringingTime = ringingTime;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public boolean isRequireRefund() {
        return requireRefund;
    }

    public void setRequireRefund(boolean requireRefund) {
        this.requireRefund = requireRefund;
    }
}
