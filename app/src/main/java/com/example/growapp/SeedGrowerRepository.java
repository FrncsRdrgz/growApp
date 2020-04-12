package com.example.growapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SeedGrowerRepository {
    private SeedGrowerDao seedGrowerDao;
    private LiveData<List<SeedGrower>> allSeedGrowers;

    public SeedGrowerRepository(Application application) {
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);

        seedGrowerDao = database.seedGrowerDao();

        allSeedGrowers = seedGrowerDao.getAll();
    }

    public void insert(SeedGrower seedGrower) {
        new InsertSeedGrowerAsyncTask(seedGrowerDao).execute(seedGrower);
    }

    public void update(SeedGrower seedGrower) {
        new UpdateSeedGrowerAsyncTask(seedGrowerDao).execute(seedGrower);
    }
    public void delete(SeedGrower seedGrower) {
        new DeleteSeedGrowerAsyncTask(seedGrowerDao).execute(seedGrower);
    }
    public void deleteAll() {

    }

    public LiveData<List<SeedGrower>> getAllSeedGrowers() {
        return allSeedGrowers;
    }

    private static class InsertSeedGrowerAsyncTask extends AsyncTask<SeedGrower, Void, Void> {
        private SeedGrowerDao seedGrowerDao;

        private InsertSeedGrowerAsyncTask(SeedGrowerDao seedGrowerDao) {
            this.seedGrowerDao = seedGrowerDao;
        }
        @Override
        protected Void doInBackground(SeedGrower... seedGrowers) {
            seedGrowerDao.insertSeedGrower(seedGrowers[0]);
            return null;
        }
    }

    private static class UpdateSeedGrowerAsyncTask extends AsyncTask<SeedGrower, Void, Void> {
        private SeedGrowerDao seedGrowerDao;

        private UpdateSeedGrowerAsyncTask(SeedGrowerDao seedGrowerDao) {
            this.seedGrowerDao = seedGrowerDao;
        }
        @Override
        protected Void doInBackground(SeedGrower... seedGrowers) {
            seedGrowerDao.updateSeedGrower(seedGrowers[0]);
            return null;
        }
    }

    private static class DeleteSeedGrowerAsyncTask extends AsyncTask<SeedGrower, Void, Void> {
        private SeedGrowerDao seedGrowerDao;

        private DeleteSeedGrowerAsyncTask(SeedGrowerDao seedGrowerDao) {
            this.seedGrowerDao = seedGrowerDao;
        }
        @Override
        protected Void doInBackground(SeedGrower... seedGrowers) {
            seedGrowerDao.deleteSeedGrower(seedGrowers[0]);
            return null;
        }
    }

}
