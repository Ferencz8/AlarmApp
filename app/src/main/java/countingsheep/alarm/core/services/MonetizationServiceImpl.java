package countingsheep.alarm.core.services;

import android.content.SharedPreferences;

import countingsheep.alarm.core.contracts.data.MonetizationRepository;
import countingsheep.alarm.core.services.interfaces.MonetizationService;
import countingsheep.alarm.db.entities.Monetization;

public class MonetizationServiceImpl implements MonetizationService {

    private MonetizationRepository monetizationRepository;
    private SharedPreferences sharedPreferences;
    private TimeService timeService;

    public MonetizationServiceImpl(MonetizationRepository monetizationRepository,
                                   SharedPreferences sharedPreferences,
                                   TimeService timeService) {
        this.monetizationRepository = monetizationRepository;
        this.sharedPreferences = sharedPreferences;
        this.timeService = timeService;
    }

    @Override
    public void add(int typeId, int chosenCost) {
        Monetization monetization = new Monetization();
        monetization.setTypeId(typeId);
        monetization.setChoosenCostPerSnooze(chosenCost);
        monetization.setDateChosen(this.timeService.getUTCDateNow());
        this.monetizationRepository.insert(monetization);

        this.sharedPreferences.edit().putString("MonetizationType",String.valueOf(typeId));
        this.sharedPreferences.edit().putString("MonetizationCost",String.valueOf(chosenCost));
    }

    @Override
    public void update(int typeId, int chosenCost) {
        this.add(typeId, chosenCost);
    }
}
