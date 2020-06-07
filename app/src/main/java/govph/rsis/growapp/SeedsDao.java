package govph.rsis.growapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SeedsDao {

    @Query("SELECT * FROM seeds")
    List<Seeds> getSeeds();

    @Query("SELECT count(*) FROM seeds")
    int isEmpty();
    @Insert
    void insertSeeds(Seeds seeds);

    @Update
    void updateSeeds(Seeds seeds);
}
