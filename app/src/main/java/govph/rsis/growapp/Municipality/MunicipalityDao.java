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

    @Insert
    void insert(Municipality municipality);

    @Update
    void update(Municipality municipality);

    @Delete
    void delete(Municipality municipality);
}
