package dataaccess;

import java.util.HashMap;
import java.util.UUID;

import Model.UserData;

public class MemoryAuthDAO implements AuthDAO{


    private HashMap<String, String> allAuthDataStorage;

    public MemoryAuthDAO(){
        this.allAuthDataStorage= new HashMap<>();
    }

    public HashMap<String,String> getAuthHashMap(){
        return allAuthDataStorage;
    }
    @Override
    public void createAuth(String newUser) {
        String authToken = UUID.randomUUID().toString();;
        allAuthDataStorage.put(newUser, authToken);
    }

    @Override
    public String getAuth(String username) {
        return allAuthDataStorage.get(username);
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {
        allAuthDataStorage.clear();
    }

}
