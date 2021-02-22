package govph.rsis.growapp.User;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import govph.rsis.growapp.SeedGrowerDatabase;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private int checkLoggedIn;
    private int isExisting;
    private int updateUserStatus;
    private User loggedInUser;

    public UserRepository(Application application){
        SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getUsers();
        checkLoggedIn = userDao.checkLoggedIn();
        loggedInUser = userDao.getLoggedInUser();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }
    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }
    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public int getCheckLoggedIn(){return checkLoggedIn;}
    public int isExisting(String serialNum){ return isExisting = userDao.isExisting(serialNum);}
    public int getUpdateUserStatus(String isLoggedIn, String serialNum){ return updateUserStatus = userDao.updateUserStatus(isLoggedIn,serialNum);}
    public User getLoggedInUser(){return loggedInUser;}

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users[0]);
            return null;
        }
    }
}
