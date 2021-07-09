package govph.rsis.growapp.Municipality;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import govph.rsis.growapp.SeedGrowerDatabase;

public class MunicipalityRepository {
    private MunicipalityDao municipalityDao;
    private List<Municipality> municipalityList;

    public MunicipalityRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        municipalityDao = database.municipalityDao();

        municipalityList = municipalityDao.getMunicipalities();
    }

    public List<Municipality> getMunicipalityList() { return municipalityList;}
    public void insert(Municipality municipality) { new InsertMunicipalityAsyncTask(municipalityDao).execute(municipality);}
    public void update(Municipality municipality) {new UpdateMunicipalityAsyncTask(municipalityDao).execute(municipality);}

    private static class InsertMunicipalityAsyncTask extends AsyncTask<Municipality, Void, Void>{
        private MunicipalityDao municipalityDao;

        private InsertMunicipalityAsyncTask(MunicipalityDao municipalityDao){ this.municipalityDao = municipalityDao;}

        @Override
        protected Void doInBackground(Municipality... municipalities) {
            municipalityDao.insert(municipalities[0]);
            return null;
        }
    }

    private static class UpdateMunicipalityAsyncTask extends AsyncTask<Municipality, Void, Void> {
        private MunicipalityDao municipalityDao;

        private UpdateMunicipalityAsyncTask(MunicipalityDao municipalityDao){ this.municipalityDao = municipalityDao;}

        @Override
        protected Void doInBackground(Municipality... municipalities) {
            municipalityDao.update(municipalities[0]);
            return null;
        }
    }


}
