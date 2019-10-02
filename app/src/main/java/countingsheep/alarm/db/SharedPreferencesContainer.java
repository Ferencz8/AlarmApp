package countingsheep.alarm.db;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.CreditsDto;

@Singleton
public class SharedPreferencesContainer {

    private SharedPreferences sharedPreferences;

    private final static String UserId = "UserId";
    private final static String PhoneNumber = "phoneNumber";
    private final static String MonetizationType = "MonetizationType";
    private final static String MonetizationCost = "MonetizationCost";
    private final static String BootReceivedPermissionOnSpecialDevices = "BootReceivedPermissionOnSpecialDevices";
    private final static String CustomerId = "CustomerId";
    private final static String Token = "Token";
    private final static String DefaultSnoozePrice="DefaultSnoozePrice";
    private final static String FreeCredits="FreeCredits";
    private final static String HasEndlessAccount="HasEndlessAccount";
    private final static String MoneySpentOnSnooze = "MoneySpentOnSnooze";
    private final static String ProfilePictureUrl = "ProfilePictureUrl";
    private final static String ProfilePicturePath = "ProfilePicturePath";
    private final static String Fullname = "Fullname";
    private final static String DisplayPaymentInOnBoarding = "DisplayPaymentInOnBoarding";
    private final static String ShouldGiveFreeCredits = "ShouldGiveFreeCredits";
    private final static String AlarmsSetCount = "AlarmsSetCount";
    private final static String NeedToRequestSMSRoastCount = "NeedToRequestSMSRoastCount";
    private final static String NeedToRequestRoastCount="NeedToRequestRoastCount";
    private final static String UserInitiatedShare = "UserInitiatedShare";
    private final static String UsernameTokens = "UsernameTokens";
    private final static String DelayTheAlarmForFree = "DelayTheAlarmForFree";
    private final static String EternalCredits = "EternalCredits";
    private final static String FreeCreditsWereAcquired = "FreeCreditsWereAcquired";

    private final static String Popup_ShowedRemoveAlarm = "ShowedRemoveAlarm";
    private final static String Popup_ShowedAskForPhoneNo = "ShowedAskForPhoneNo";



    @Inject
    public SharedPreferencesContainer(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public void setDelayTheAlarmForFree(boolean value){
        this.changePreferenceValue(DelayTheAlarmForFree, value);
    }

    public boolean getDelayTheAlarmForFree() {
        return this.sharedPreferences.getBoolean(DelayTheAlarmForFree, true);
    }


    public void setEternalCredits() {

        changePreferenceValue(EternalCredits, true);
    }

    public boolean getEternalCredits() {
        return this.sharedPreferences.getBoolean(EternalCredits, false);
    }

    public void increaseUsernameTokensCount() {
        int currentValue = getUsernameTokensCount();
        changePreferenceValue(UsernameTokens, ++currentValue);
    }

    public int getUsernameTokensCount() {
        return this.sharedPreferences.getInt(UsernameTokens, 0);
    }

    public void resetUsernameTokensCount() {
        changePreferenceValue(UsernameTokens, 0);
    }

    public void setUserInitiatedShare(boolean value){
        this.changePreferenceValue(UserInitiatedShare, value);
    }

    public boolean getUserInitiatedShare() {
        return this.sharedPreferences.getBoolean(UserInitiatedShare, false);
    }

    public void resetNeededToRequestRoastCount() {
        changePreferenceValue(NeedToRequestRoastCount, 0);
    }

    public void increaseNeededToRequestRoastCount() {
        int currentValue = getNeedToRequestSMSRoastCount();
        changePreferenceValue(NeedToRequestRoastCount, ++currentValue);
    }

    public int getNeedToRequestRoastCount() {
        return this.sharedPreferences.getInt(NeedToRequestRoastCount, 0);
    }

    public void resetNeededToRequestSMSRoastCount() {
        changePreferenceValue(NeedToRequestSMSRoastCount, 0);
    }

    public void increaseNeededToRequestSMSRoastCount() {
        int currentValue = getNeedToRequestSMSRoastCount();
        changePreferenceValue(NeedToRequestSMSRoastCount, ++currentValue);
    }

    public int getNeedToRequestSMSRoastCount() {
        return this.sharedPreferences.getInt(NeedToRequestSMSRoastCount, 0);
    }


    public void increaseSetAlarmsCount() {
        int currentValue = getAlarmsCountThatWereSet();
        changePreferenceValue(AlarmsSetCount, ++currentValue);
    }

    public int getAlarmsCountThatWereSet() {
        return this.sharedPreferences.getInt(AlarmsSetCount, 0);
    }

    public boolean getShowedAskForPhoneNoPopup() {
        return this.sharedPreferences.getBoolean(Popup_ShowedAskForPhoneNo, false);
    }

    public void setShowedAskForPhoneNoPopup() {
        changePreferenceValue(Popup_ShowedAskForPhoneNo, true);
    }

    public void setPopopShowedRemoveAlarm(){ changePreferenceValue(Popup_ShowedRemoveAlarm, true); }

    public boolean getPopopShowedRemoveAlarm(){ return this.sharedPreferences.getBoolean(Popup_ShowedRemoveAlarm, false); }

    public int getDefaultSnoozePrice(){return this.sharedPreferences.getInt(DefaultSnoozePrice, 1);}

    public void setDefaultSnoozePrice(int price){changePreferenceValue(DefaultSnoozePrice, price);}

    public boolean getDisplayPaymentInOnBoarding(){
        return this.sharedPreferences.getBoolean(DisplayPaymentInOnBoarding, false);
    }
    public void setDisplayPaymentInOnBoarding(boolean value){
        changePreferenceValue(DisplayPaymentInOnBoarding, value);
    }

    public boolean shouldGiveFreeCredits(){
        return this.sharedPreferences.getBoolean(ShouldGiveFreeCredits, false);
    }

    public void allowFreeCredits(boolean value){
        changePreferenceValue(ShouldGiveFreeCredits, value);
    }

    public void increaseFreeCredits(int amount){
        int currentValue = getFreeCredits();
        setFreeCredits(amount + currentValue);
    }

    public int getFreeCredits(){
        return this.sharedPreferences.getInt(FreeCredits, 0);
    }

    public void setFreeCredits(int amount) {
        changePreferenceValue(FreeCredits, amount);
    }

    public boolean hasEndlessAccount(){
        return this.sharedPreferences.getBoolean(HasEndlessAccount, false);
    }

    public void setHasEndlessAccount(boolean hasEndlessAccount) {
        changePreferenceValue(HasEndlessAccount, hasEndlessAccount);
    }

    public boolean doesAllPaymentInformationExist(){
        return doesTokenExist() && doesCustomerExist();
    }

    public boolean doesTokenExist(){
        String token = getToken();
        return token !=null && token !="";
    }

    public String getToken(){return this.sharedPreferences.getString(Token, "");}

    public void setToken(String token){changePreferenceValue(Token, token);}

    public String getCustomerId(){return this.sharedPreferences.getString(CustomerId, null);}

    public void setCustomerId(String customerId){changePreferenceValue(CustomerId, customerId);}

    public boolean doesCustomerExist(){ return getCustomerId()!=null;}


    public String getProfilePictureLocalPath() {
        return this.sharedPreferences.getString(ProfilePicturePath, "");
    }

    public void setProfilePictureLocalPath(String profilePicturePath) {
        changePreferenceValue(ProfilePicturePath, profilePicturePath);
    }

    public String getProfilePictureUrl() {
        return this.sharedPreferences.getString(ProfilePictureUrl, "");
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        changePreferenceValue(ProfilePictureUrl, profilePictureUrl);
    }

    public String getFullname() {
        return this.sharedPreferences.getString(Fullname, "");
    }

    public void setFullname(String fullname) {
        changePreferenceValue(Fullname, fullname);
    }

    public int getCurrentUserId(){
        return this.sharedPreferences.getInt(UserId, 0);
    }

    public void setCurrentUserId(int currentUserId) {
        changePreferenceValue(UserId, currentUserId);
    }

    public String getCurrentUserPhoneNumber(){
        return this.sharedPreferences.getString(PhoneNumber, "");
    }

    public void setCurrentUserPhoneNumber(String phoneNumber) {
        changePreferenceValue(PhoneNumber, phoneNumber);
    }

    public boolean doesUserIdExist(){
        int UserDoesNotExistValue = 0;
        return this.getCurrentUserId()!= UserDoesNotExistValue;
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
        synchronized (this) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            }

            editor.apply();
        }
    }
}
