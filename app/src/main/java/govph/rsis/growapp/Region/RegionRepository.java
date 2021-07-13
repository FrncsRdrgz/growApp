package govph.rsis.growapp.Region;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import govph.rsis.growapp.Municipality.Municipality;
import govph.rsis.growapp.Municipality.MunicipalityDao;
import govph.rsis.growapp.Municipality.MunicipalityRepository;
import govph.rsis.growapp.SeedGrowerDatabase;

public class RegionRepository {
    private RegionDao regionDao;
    private List<Region> regionList;

    public RegionRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        regionDao = database.regionDao();
        regionList = regionDao.getRegions();
    }

    public List<Region> getRegionList(){return regionList;}

    public void insert(Region region){new InsertRegionAsyncTask(regionDao).execute(region);}
    public void update(Region region){new UpdateRegionAsyncTask(regionDao).execute(region);}
    public void deleteRegions(){regionDao.deleteRegions();}

    private static class InsertRegionAsyncTask extends AsyncTask<Region, Void, Void> {
        private RegionDao regionDao;

        private InsertRegionAsyncTask(RegionDao regionDao){ this.regionDao = regionDao;}

        @Override
        protected Void doInBackground(Region... regions) {
            regionDao.insert(regions[0]);
            return null;
        }
    }

    private static class UpdateRegionAsyncTask extends AsyncTask<Region, Void, Void> {
        private RegionDao regionDao;

        private UpdateRegionAsyncTask(RegionDao regionDao) {this.regionDao = regionDao;}

        @Override
        protected Void doInBackground(Region... regions) {
            regionDao.update(regions[0]);
            return null;
        }
    }
}
