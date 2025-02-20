package dataaccess;

import Model.AuthData;

//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    void createAuth();
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
