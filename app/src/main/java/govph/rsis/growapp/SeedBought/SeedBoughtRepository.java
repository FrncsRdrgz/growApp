package govph.rsis.growapp.SeedBought;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import govph.rsis.growapp.SeedGrowerDatabase;

public class SeedBoughtRepository {
    private SeedBoughtDao seedBoughtDao;
    private List<SeedBought> allSeedBought;
    private int updateUsedQuantity;
    private int returnQuantity;
    private SeedBought seedBought;

    public SeedBoughtRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        seedBoughtDao = database.seedBoughtDao();
    }

    public void insert(SeedBought seedBought) {new InsertAsyncTask(seedBoughtDao).execute(seedBought);}

    public int  updateUsedQuantity(String serialNum,int usedQuantity, int id){
        return updateUsedQuantity = seedBoughtDao.updateUsedQuantity(serialNum, usedQuantity, id);
    }

    public int getReturnQuantity(String serialNum, int id, int usedQuantity){
        return returnQuantity = seedBoughtDao.returnQuantity(serialNum, id, usedQuantity);
    }

    public List<SeedBought> getAllSeedBought(String serialNum,String station_name){
        return allSeedBought = seedBoughtDao.getSeedBought(serialNum, station_name);
    }

    public SeedBought seedBought(int id){
        return seedBought = seedBoughtDao.getById(id);
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
