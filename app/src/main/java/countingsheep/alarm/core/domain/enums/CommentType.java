package countingsheep.alarm.core.domain.enums;

public enum CommentType {
    RoastResponse(0),
    Feedback(1);

    private int code;

    CommentType(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
