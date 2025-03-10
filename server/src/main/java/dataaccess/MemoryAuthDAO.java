package dataaccess;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{


    private HashMap<String, String> allAuthDataStorage;

    public MemoryAuthDAO(){
        this.allAuthDataStorage= new HashMap<>();
    }

    public HashMap<String,String> getAuthHashMap(){
        return allAuthDataStorage;
    }
    @Override
    public HashMap<String,String> grabHash(){
        return allAuthDataStorage;
    }

    @Override
    public boolean authTokenExists(String authToken) {
        if(allAuthDataStorage != null){
            return allAuthDataStorage.containsKey(authToken);
        }
        return false;
    }

    @Override
    public HashMap<String, String> getAuthHash() {
        return allAuthDataStorage;
    }

    @Override
    public String createAuth(String newUser) throws SQLException {
        String authToken = UUID.randomUUID().toString();;
        allAuthDataStorage.put(authToken, newUser);
        return authToken;
    }

    @Override
    public String getAuthUsername(String auth) {
        return allAuthDataStorage.get(auth);
    }

    @Override
    public void deleteAuth(String authToken) {
        allAuthDataStorage.remove(authToken,allAuthDataStorage.get(authToken));
    }

    @Override
    public void clear() {
        allAuthDataStorage.clear();
    }

    @Override
    public boolean usernameInAuthDatabase(String authToken) {
        if(allAuthDataStorage != null){
            return allAuthDataStorage.containsKey(authToken);
        }
        return false;
    }


}
