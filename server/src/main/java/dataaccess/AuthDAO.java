package dataaccess;

import java.util.HashMap;

//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    String createAuth(String currentUser);
    String getAuthUsername(String auth);
    void deleteAuth(String auth);
    void clear();
    boolean usernameInAuthDatabase(String authToken);
    HashMap<String,String> grabHash();
    boolean authTokenExists(String authToken);
    HashMap<String, String> getAuthHash();
}
