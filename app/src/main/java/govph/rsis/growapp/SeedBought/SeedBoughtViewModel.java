package govph.rsis.growapp.SeedBought;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class SeedBoughtViewModel extends AndroidViewModel {
    private SeedBoughtRepository seedBoughtRepository;
    private LiveData<List<SeedBought>> allSeedBought;

    public SeedBoughtViewModel(@NonNull Application application){
        super(application);

        seedBoughtRepository = new SeedBoughtRepository(application);
    }

    public void insert(SeedBought seedBought){seedBoughtRepository.insert(seedBought);}


}
