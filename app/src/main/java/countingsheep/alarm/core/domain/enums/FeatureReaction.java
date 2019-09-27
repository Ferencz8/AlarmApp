package countingsheep.alarm.core.domain.enums;

public enum FeatureReaction {
    Like(0),
    DisLike(1),
    NotReacted(2);

    private int code;

    FeatureReaction(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
