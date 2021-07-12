package govph.rsis.growapp.Municipality;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import govph.rsis.growapp.User.UserViewModel;

public class MunicipalityViewModel extends AndroidViewModel {
    private MunicipalityRepository municipalityRepository;
    private List<Municipality> municipalityList;

    public MunicipalityViewModel(@NonNull Application application){
        super(application);
        municipalityRepository = new MunicipalityRepository(application);
        municipalityList = municipalityRepository.getMunicipalityList();
    }

    public List<Municipality> getMunicipalityList(){return municipalityList;}

    public void insert(Municipality municipality) {municipalityRepository.insert(municipality);}

}