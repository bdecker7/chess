package dataaccess;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.UUID;
import Model.AuthData;
import Model.UserData;

public class MemoryAuthDAO implements AuthDAO{


    public HashMap<String, AuthData> allAuthDataStorage;

    public MemoryAuthDAO(){
        this.allAuthDataStorage= new HashMap<>();
    }

    public HashMap<String,AuthData> getAuthHashMap(){
        return allAuthDataStorage;
    }
    @Override
    public void createAuth(String username) {
        String authToken = generateToken();
        allAuthDataStorage.put(authToken,);
    }

    @Override
    public AuthData getAuth(String authToken) {

        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
