package govph.rsis.growapp.Province;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProvinceDao {

    @Query("SELECT * FROM province")
    List<Province> getProvinces();

    @Insert
    void insert(Province province);

    @Update
    void update(Province province);

    @Delete
    void delete(Province province);
}
