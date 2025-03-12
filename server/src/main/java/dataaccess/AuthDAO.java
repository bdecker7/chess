package dataaccess;

import dataaccess.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.HashMap;

//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    String createAuth(String currentUser) throws SQLException;
    String getAuthUsername(String auth) throws DataAccessException;
    void deleteAuth(String auth) throws SQLException;
    void clear();
    boolean usernameInAuthDatabase(String authToken);
    HashMap<String,String> grabHash();
    boolean authTokenExists(String authToken);
    HashMap<String, String> getAuthHash();
}
