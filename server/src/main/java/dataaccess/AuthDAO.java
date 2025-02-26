package dataaccess;

import Model.UserData;

//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    void createAuth(String currentUser);
    String getAuth(String username);
    void deleteAuth(String username);
    void clear();
}
