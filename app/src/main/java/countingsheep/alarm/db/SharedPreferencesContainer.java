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

    @Inject
    public SharedPreferencesContainer(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public int getCurrentUserId(){
        return this.sharedPreferences.getInt(UserId, 0);
    }

    public void setCurrentUserId(int currentUserId) {
        changePreferenceValue(UserId, currentUserId);
    }

    public int getMonetizationType(){return this.sharedPreferences.getInt(MonetizationType, 0);}

    public void setMonetizationType(int monetizationType){
        changePreferenceValue(MonetizationType, monetizationType);
    }

    public int getMonetizationCost(){return this.sharedPreferences.getInt(MonetizationCost, 0);}

    public void setMonetizationCost(int monetizationCost){
        changePreferenceValue(MonetizationCost, monetizationCost);
    }

    private <T> void changePreferenceValue(String key, T value){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        if(value instanceof Integer){
            editor.putInt(key, (Integer)value);
        }
        else if(value instanceof String){
            editor.putString(key, (String)value);
        }

        editor.apply();
    }
}
