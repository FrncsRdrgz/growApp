package govph.rsis.growapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SeedGrowerRepository {
    private SeedGrowerDao seedGrowerDao;
    private LiveData<List<SeedGrower>> allSeedGrowers;
    private LiveData<List<SeedGrower>> allSent;
    int countCollected,countSent,countDeleted,softDelete;

    public SeedGrowerRepository(Application application) {
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        seedGrowerDao = database.seedGrowerDao();
        //allSent = seedGrowerDao.getSentForms();
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


    public LiveData<List<SeedGrower>> getAllSeedGrowers(String accredno) {
        return allSeedGrowers = seedGrowerDao.getAll(accredno);
    }
    public LiveData<List<SeedGrower>> getAllSent(String accredno) {
        return allSent = seedGrowerDao.getSentForms(accredno);
    }

    public int getCountCollected(String accredno) {
        return countCollected = seedGrowerDao.countCollected(accredno);
    }

    public int getCountSent(String accredno) {
        return countSent = seedGrowerDao.countSent(accredno);
    }

    public int getCountDeleted(String accredno) {
        return countDeleted = seedGrowerDao.countDeleted(accredno);
    }

    public int getSoftDelete(String accredno, int status, int id){
        return softDelete = seedGrowerDao.softDelete(accredno,status,id);
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
