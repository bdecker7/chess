package dataaccess;

//clear: A method for clearing all data from the database. This is used during testing.
//createUser: Create a new user.
//getUser: Retrieve a user with the given username.

import Model.UserData;

import java.util.HashMap;

public interface UserDAO {
    HashMap<String,UserData> getUserHashMap();
    void clear();
    void createUser(UserData data);
    boolean checkUser(String username);
    UserData getUserData(String username);
}
