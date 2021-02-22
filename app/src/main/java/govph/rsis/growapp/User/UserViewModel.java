package govph.rsis.growapp.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;
    private int checkLoggedIn;
    private int isExisting;
    private int updateUserStatus;
    private User loggedInUser;
    public UserViewModel(@NonNull Application application){
        super(application);

        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUsers();
        checkLoggedIn = userRepository.getCheckLoggedIn();
        loggedInUser = userRepository.getLoggedInUser();
    }

    public void insert(User user){userRepository.insert(user);}
    public void update(User user){userRepository.update(user);}
    public void delete(User user){userRepository.delete(user);}

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public int getCheckLoggedIn(){return checkLoggedIn;}
    public int isExisting(String serialNum){return isExisting = userRepository.isExisting(serialNum);}
    public int getUpdateUserStatus(String isLoggedIn, String serialNum){return updateUserStatus = userRepository.getUpdateUserStatus(isLoggedIn,serialNum);}
    public User getLoggedInUser() {return loggedInUser;}
}
