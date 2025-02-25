package dataaccess;

import Model.UserData;

//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    void createAuth(UserData newUser);
    String getAuth(String authToken);
    void deleteAuth(String authToken);
    void clear();
}
