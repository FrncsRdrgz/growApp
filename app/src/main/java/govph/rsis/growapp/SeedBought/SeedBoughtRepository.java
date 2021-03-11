package govph.rsis.growapp.SeedBought;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import govph.rsis.growapp.SeedGrowerDatabase;

public class SeedBoughtRepository {
    private SeedBoughtDao seedBoughtDao;
    private List<SeedBought> allSeedBought;

    public SeedBoughtRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        seedBoughtDao = database.seedBoughtDao();
    }

    public void insert(SeedBought seedBought) {new InsertAsyncTask(seedBoughtDao).execute(seedBought);}

    public List<SeedBought> getAllSeedBought(String serialNum){
        return allSeedBought = seedBoughtDao.getSeedBought(serialNum);
    }
    private static class InsertAsyncTask extends AsyncTask<SeedBought, Void, Void> {
        private SeedBoughtDao seedBoughtDao;

        private InsertAsyncTask(SeedBoughtDao seedBoughtDao){this.seedBoughtDao = seedBoughtDao;}

        @Override
        protected Void doInBackground(SeedBought... seedBoughts) {
            seedBoughtDao.insert(seedBoughts[0]);

            return null;
        }
    }
}
