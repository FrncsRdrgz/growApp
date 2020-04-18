package govph.rsis.growapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SeedGrowerRepository {
    private SeedGrowerDao seedGrowerDao;
    private LiveData<List<SeedGrower>> allSeedGrowers;
    private LiveData<List<SeedGrower>> allSent;

    public SeedGrowerRepository(Application application) {
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        seedGrowerDao = database.seedGrowerDao();
        allSeedGrowers = seedGrowerDao.getAll();
        allSent = seedGrowerDao.getSentForms();
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

    public void deleteAllSeedGrower() {
        new DeleteAllSeedGrowerAsyncTask(seedGrowerDao).execute();
    }


    public LiveData<List<SeedGrower>> getAllSeedGrowers() {
        return allSeedGrowers;
    }
    public LiveData<List<SeedGrower>> getAllSent() {
        return allSent;
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

    private static class DeleteAllSeedGrowerAsyncTask extends AsyncTask<Void, Void, Void> {
        private SeedGrowerDao seedGrowerDao;

        private DeleteAllSeedGrowerAsyncTask(SeedGrowerDao seedGrowerDao) {
            this.seedGrowerDao = seedGrowerDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            seedGrowerDao.deleteAll();
            return null;
        }
    }
}
