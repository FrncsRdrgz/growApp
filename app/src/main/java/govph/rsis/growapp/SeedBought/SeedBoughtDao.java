package govph.rsis.growapp.SeedBought;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SeedBoughtDao {

    @Query("SELECT * from seedbought WHERE quantity !=usedQuantity AND serialNum =:serialNum AND station_name =:station_name ORDER BY id")
    List<SeedBought> getSeedBought(String serialNum,String station_name);

    @Query("SELECT * from seedbought WHERE id=:id")
    SeedBought getById(int id);

    @Query("UPDATE seedbought SET usedQuantity =:usedQuantity WHERE  serialNum =:serialNum AND id =:id")
    int updateUsedQuantity(String serialNum, int usedQuantity, int id);

    @Query("UPDATE seedbought SET usedQuantity =:usedQuantity WHERE serialNum =:serialNum AND id=:id")
    int returnQuantity(String serialNum, int id, int usedQuantity);

    @Insert
    void insert(SeedBought seedBought);

    @Update
    void update(SeedBought seedBought);

    @Delete
    void delete(SeedBought seedBought);
}
