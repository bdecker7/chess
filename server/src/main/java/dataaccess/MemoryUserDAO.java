package dataaccess;

import Handler.RegisterRequest;
import Model.AuthData;
import Model.UserData;
import Service.RegisterResult;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private HashMap<String,UserData> allUsersDataStorage;

    public MemoryUserDAO(){
        this.allUsersDataStorage= new HashMap<>();
    }

    public HashMap<String,UserData> getUserHashMap(){
        return allUsersDataStorage;
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
        // then creates the userdata, then auth data.
        if(allUsersDataStorage != null){
            return allUsersDataStorage.containsKey(username);
        }
        return false;
    }
}
