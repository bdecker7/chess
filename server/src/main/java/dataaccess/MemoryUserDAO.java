package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private HashMap<String,UserData> allUsersDataStorage;

    public MemoryUserDAO(){
        this.allUsersDataStorage= new HashMap<>();
    }

    @Override
    public void clear() {
        allUsersDataStorage.clear();
    }

    @Override
   public void createUser(UserData newUser) {
        UserData currentNewUser = new UserData(newUser.username(),newUser.password(),newUser.email());
        allUsersDataStorage.put(newUser.username(),currentNewUser);
    }

    @Override
    public boolean checkUser(String username){
        //checks to see if the username is in the database
        if(allUsersDataStorage != null){
            return allUsersDataStorage.containsKey(username);
        }
        return false;
    }

    @Override
    public UserData getUserData(String username) {
        return allUsersDataStorage.get(username);
    }
}
