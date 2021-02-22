package govph.rsis.growapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SeedGrowerViewModel extends AndroidViewModel {
    private SeedGrowerRepository repository;
    private LiveData<List<SeedGrower>> allSeedGrowers;
    private LiveData<List<SeedGrower>> allSent;
    public SeedGrowerViewModel(@NonNull Application application) {
        super(application);

        repository = new SeedGrowerRepository(application);
        //allSeedGrowers = repository.getAllSeedGrowers();
        //allSent = repository.getAllSent();
    }

    public void insert(SeedGrower seedGrower) {
        repository.insert(seedGrower);
    }

    public void update(SeedGrower seedGrower) {
        repository.update(seedGrower);
    }

    public void delete(SeedGrower seedGrower) {
        repository.delete(seedGrower);
    }

    public void deleteAllSeedGrower() {repository.deleteAllSeedGrower();}

    public LiveData<List<SeedGrower>> getAllSeedGrowers(String accredno) {
        return allSeedGrowers = repository.getAllSeedGrowers(accredno);
    }

    public LiveData<List<SeedGrower>> getAllSent(String accredno) {return allSent = repository.getAllSent(accredno);}
}
