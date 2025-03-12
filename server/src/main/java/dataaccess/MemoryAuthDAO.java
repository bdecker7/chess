package dataaccess;

import dataaccess.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{


    private HashMap<String, String> allAuthDataStorage;

    public MemoryAuthDAO(){
        this.allAuthDataStorage= new HashMap<>();
    }


    @Override
    public boolean authTokenExists(String authToken) {
        if(allAuthDataStorage != null){
            return allAuthDataStorage.containsKey(authToken);
        }
        return false;
    }


    @Override
    public String createAuth(String newUser) throws SQLException {
        String authToken = UUID.randomUUID().toString();;
        allAuthDataStorage.put(authToken, newUser);
        return authToken;
    }

    @Override
    public String getAuthUsername(String auth) throws DataAccessException {
        return allAuthDataStorage.get(auth);
    }

    @Override
    public void deleteAuth(String authToken) throws SQLException {
        allAuthDataStorage.remove(authToken,allAuthDataStorage.get(authToken));
    }

    @Override
    public void clear() {
        allAuthDataStorage.clear();
    }


}
