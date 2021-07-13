package govph.rsis.growapp.Region;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class RegionViewModel extends AndroidViewModel {
    private RegionRepository regionRepository;
    private List<Region> regionList;

    public RegionViewModel(@NonNull Application application){
        super(application);

        regionRepository = new RegionRepository(application);
        regionList = regionRepository.getRegionList();
    }

    public List<Region> getRegionList() { return regionList; }
    public void insert(Region region){regionRepository.insert(region);}
    public void update(Region region){regionRepository.update(region);}
    public void deleteRegions(){regionRepository.deleteRegions();}
}
