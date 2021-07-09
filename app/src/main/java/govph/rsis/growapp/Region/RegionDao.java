package govph.rsis.growapp.Region;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RegionDao {

    @Query("SELECT * FROM region")
    List<Region> getRegions();


    @Insert
    void insert(Region region);

    @Update
    void update(Region region);

    @Delete
    void delete(Region region);
}
