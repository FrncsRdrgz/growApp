package govph.rsis.growapp.SeedBought;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class SeedBoughtViewModel extends AndroidViewModel {
    private SeedBoughtRepository seedBoughtRepository;
    private List<SeedBought> allSeedBought;
    private SeedBought seedBought;
    private int updateUsedQuantity;
    private int returnQuantity;

    public SeedBoughtViewModel(@NonNull Application application){
        super(application);

        seedBoughtRepository = new SeedBoughtRepository(application);
    }

    public void insert(SeedBought seedBought){seedBoughtRepository.insert(seedBought);}

    public int getReturnQuantity(String serialNum, int id, int usedQuantity){
        return returnQuantity = seedBoughtRepository.getReturnQuantity(serialNum, id, usedQuantity);
    }
    public int getUpdateUsedQuantity(String serialNum, int usedQuantity, int id) {
        return updateUsedQuantity = seedBoughtRepository.updateUsedQuantity(serialNum,usedQuantity,id);
    }

    public SeedBought seedBought(int id){
        return seedBought = seedBoughtRepository.seedBought(id);
    }
    public List<SeedBought> getAllSeedBought(String serialNum,String station_name){
        return allSeedBought = seedBoughtRepository.getAllSeedBought(serialNum,station_name);
    }

}
