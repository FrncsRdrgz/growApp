package govph.rsis.growapp.Province;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import govph.rsis.growapp.Municipality.Municipality;
import govph.rsis.growapp.Municipality.MunicipalityDao;
import govph.rsis.growapp.Municipality.MunicipalityRepository;
import govph.rsis.growapp.SeedGrowerDatabase;

public class ProvinceRepository {
    private ProvinceDao provinceDao;
    private List<Province> provinceList;

    public ProvinceRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        provinceDao = database.provinceDao();

        provinceList = provinceDao.getProvinces();
    }

    public List<Province> getProvinceList() { return provinceList; }
    public List<Province> getProvinceByRegionId(int region_id){return this.provinceDao.getProvinceByRegionId(region_id);}
    public void insert(Province province) { new InsertProvinceAsyncTask(provinceDao).execute(province);}
    public void update(Province province) {new UpdateProvinceAsyncTask(provinceDao).execute(province);}
    public void deleteProvinces(){provinceDao.deleteProvinces();}

    private static class InsertProvinceAsyncTask extends AsyncTask<Province, Void, Void> {
        private ProvinceDao provinceDao;

        private InsertProvinceAsyncTask(ProvinceDao provinceDao){ this.provinceDao = provinceDao;}

        @Override
        protected Void doInBackground(Province... provinces) {
            provinceDao.insert(provinces[0]);
            return null;
        }
    }

    private static class UpdateProvinceAsyncTask extends AsyncTask<Province, Void, Void> {
        private ProvinceDao provinceDao;

        private UpdateProvinceAsyncTask(ProvinceDao provinceDao){ this.provinceDao = provinceDao;}

        @Override
        protected Void doInBackground(Province... provinces) {
            provinceDao.insert(provinces[0]);
            return null;
        }
    }

}
