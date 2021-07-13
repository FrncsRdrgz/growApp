package govph.rsis.growapp.Municipality;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MunicipalityDao {

    @Query("SELECT * FROM municipality")
    List<Municipality> getMunicipalities();

    @Query("SELECT * FROM municipality WHERE province_id =:province_id")
    List<Municipality> getMunicipalitiesByProvinceId(int province_id);

    @Query("DELETE FROM municipality")
    void deleteMunicipalities();
    @Insert
    void insert(Municipality municipality);

    @Update
    void update(Municipality municipality);

    @Delete
    void delete(Municipality municipality);
}
