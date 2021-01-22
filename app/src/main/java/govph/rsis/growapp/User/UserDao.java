package govph.rsis.growapp.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getUsers();

    @Query("SELECT count(*) FROM user")
    int isEmpty();

    @Insert
    void insetUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
