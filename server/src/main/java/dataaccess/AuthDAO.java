package dataaccess;
//createAuth: Create a new authorization.
//getAuth: Retrieve an authorization given an authToken.
//deleteAuth: Delete an authorization so that it is no longer valid.
public interface AuthDAO {
    void createAuth();
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
