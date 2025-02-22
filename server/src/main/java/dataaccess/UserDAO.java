package dataaccess;

//clear: A method for clearing all data from the database. This is used during testing.
//createUser: Create a new user.
//getUser: Retrieve a user with the given username.

import Model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData data);
    boolean getUser(String username);
}
