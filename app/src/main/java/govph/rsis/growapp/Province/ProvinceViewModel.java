package govph.rsis.growapp.Province;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class ProvinceViewModel extends AndroidViewModel {
    private ProvinceRepository provinceRepository;
    private List<Province>  provinceList;

    public ProvinceViewModel(@NonNull Application application){
        super(application);
        provinceRepository = new ProvinceRepository(application);
        provinceList = provinceRepository.getProvinceList();
    }

    public List<Province> getProvinceList(){return provinceList;}
    public List<Province> getProvinceByRegionId(int region_id){return provinceRepository.getProvinceByRegionId(region_id);}
    public void insert(Province province){provinceRepository.insert(province);}
    public void update(Province province){provinceRepository.update(province);}
    public void deleteProvinces(){provinceRepository.deleteProvinces();}
}
