package countingsheep.alarm.core.services.interfaces;

public interface MonetizationService {

    //adds a new means of monetization
    void add(int typeId, int chosenCost);

    //adds a new record of monetization(to keep history), which will be considered tha latest
    void update(int typeId, int chosenCost);
}
