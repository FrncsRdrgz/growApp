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

    @Query("SELECT * FROM user where isLoggedIn = 'LoggedIn' ")
    int checkLoggedIn();

    @Query("SELECT * FROM user where isLoggedIn = 'LoggedIn' ")
    User getLoggedInUser();

    @Query("SELECT COUNT(*) FROM user where serialNum =:serialNum")
    int isExisting(String serialNum);

    @Query("UPDATE user SET isLoggedIn =:isLoggedIn WHERE  serialNum =:serialNum")
    int updateUserStatus(String isLoggedIn, String serialNum);
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
