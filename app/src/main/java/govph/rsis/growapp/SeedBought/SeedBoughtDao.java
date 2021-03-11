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

    @Query("SELECT * from seedbought WHERE serialNum =:serialNum ORDER BY id")
    List<SeedBought> getSeedBought(String serialNum);

    @Insert
    void insert(SeedBought seedBought);

    @Update
    void update(SeedBought seedBought);

    @Delete
    void delete(SeedBought seedBought);
}
