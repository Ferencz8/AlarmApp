package countingsheep.alarm.db.entities;

public enum PaymentStatus {
    Requested(0),
    Settled(1),
    Failed(2);

    private int code;

    PaymentStatus(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
