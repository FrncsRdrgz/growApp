package com.example.growapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SeedGrowerViewModel extends AndroidViewModel {
    private SeedGrowerRepository repository;
    private LiveData<List<SeedGrower>> allSeedGrowers;

    public SeedGrowerViewModel(@NonNull Application application) {
        super(application);

        repository = new SeedGrowerRepository(application);
        allSeedGrowers = repository.getAllSeedGrowers();
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

    public LiveData<List<SeedGrower>> getAllSeedGrowers() {
        return allSeedGrowers;
    }
}
