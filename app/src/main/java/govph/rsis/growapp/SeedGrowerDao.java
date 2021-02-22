package govph.rsis.growapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SeedGrowerDao {

    @Query("SELECT * from seedgrower WHERE isSent=0 AND accredno =:accredno ORDER BY sgId DESC")
    LiveData<List<SeedGrower>> getAll(String accredno);

    @Query("SELECT * from seedgrower WHERE isSent=1 AND accredno =:accredno ORDER BY sgId DESC")
    LiveData<List<SeedGrower>> getSentForms(String accredno);

    @Query("SELECT * from seedgrower WHERE sgId =:sgId")
    SeedGrower findFormById(String sgId);

    @Query("DELETE FROM seedgrower")
    void deleteAll();

    @Insert
    void insertSeedGrower(SeedGrower seedGrower);

    @Update
    void updateSeedGrower(SeedGrower seedGrower);

    @Delete
    void deleteSeedGrower(SeedGrower seedGrower);
}
