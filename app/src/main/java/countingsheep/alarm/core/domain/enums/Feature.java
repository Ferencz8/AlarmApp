package countingsheep.alarm.core.domain.enums;

public enum Feature {

    Battle(0),
    RoastChat(1),
    LeaderBoard(2),
    RoastAFriend(3);

    private int code;

    Feature(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
