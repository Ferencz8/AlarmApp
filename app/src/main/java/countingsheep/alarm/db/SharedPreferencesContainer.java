package countingsheep.alarm.db;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesContainer {

    private SharedPreferences sharedPreferences;

    private final static String UserId = "UserId";
    private final static String MonetizationType = "MonetizationType";
    private final static String MonetizationCost = "MonetizationCost";
    private final static String BootReceivedPermissionOnSpecialDevices = "BootReceivedPermissionOnSpecialDevices";
    private final static String CustomerId = "CustomerId";
    private final static String Token = "Token";
    private final static String DefaultSnoozePrice="DefaultSnoozePrice";
    private final static String FreeCredits="FreeCredits";
    private final static String MoneySpentOnSnooze = "MoneySpentOnSnooze";

    private final static String Popup_ShowedRemoveAlarm = "ShowedRemoveAlarm";


    @Inject
    public SharedPreferencesContainer(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public void setPopopShowedRemoveAlarm(){ changePreferenceValue(Popup_ShowedRemoveAlarm, true); }

    public boolean getPopopShowedRemoveAlarm(){ return this.sharedPreferences.getBoolean(Popup_ShowedRemoveAlarm, false); }

    public int getDefaultSnoozePrice(){return this.sharedPreferences.getInt(DefaultSnoozePrice, 0);}

    public void setDefaultSnoozePrice(int price){changePreferenceValue(DefaultSnoozePrice, price);}

    public String getToken(){return this.sharedPreferences.getString(Token, "");}

    public void setToken(String token){changePreferenceValue(Token, token);}

    public String getCustomerId(){return this.sharedPreferences.getString(CustomerId, null);}

    public void setCustomerId(String customerId){changePreferenceValue(CustomerId, customerId);}

    public boolean doesCustomerExist(){ return getCustomerId()!=null;}

    public int getCurrentUserId(){
        return this.sharedPreferences.getInt(UserId, 0);
    }

    public void setCurrentUserId(int currentUserId) {
        changePreferenceValue(UserId, currentUserId);
    }

    public boolean doesUserIdExist(){
        int UserDoesNotExistValue = 0;
        return this.getCurrentUserId()!= UserDoesNotExistValue;
    }

    public int getFreeCredits(){
        return this.sharedPreferences.getInt(FreeCredits, 0);
    }

    public void setFreeCredits(int amount) {
        changePreferenceValue(FreeCredits, amount);
    }

    public int getMoneySpentOnSnooze(){ return this.sharedPreferences.getInt(MoneySpentOnSnooze, 0); }

    public void setMoneySpentOnSnooze(int amount) { changePreferenceValue(MoneySpentOnSnooze, amount); }

    public int getMonetizationType(){return this.sharedPreferences.getInt(MonetizationType, 0);}

    public void setMonetizationType(int monetizationType){
        changePreferenceValue(MonetizationType, monetizationType);
    }

    public int getMonetizationCost(){return this.sharedPreferences.getInt(MonetizationCost, 0);}

    public void setMonetizationCost(int monetizationCost){
        changePreferenceValue(MonetizationCost, monetizationCost);
    }

    public boolean getBootReceivedOnSpecialDevices(){
        return  this.sharedPreferences.getBoolean(BootReceivedPermissionOnSpecialDevices, false);
    }

    public void setBootReceivedOnSpecialDevices(){
        changePreferenceValue(BootReceivedPermissionOnSpecialDevices, true);
    }

    private <T> void changePreferenceValue(String key, T value){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        if(value instanceof Integer){
            editor.putInt(key, (Integer)value);
        }
        else if(value instanceof String){
            editor.putString(key, (String)value);
        }
        else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }

        editor.apply();
    }
}
