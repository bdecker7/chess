package dataaccess;

import java.util.Dictionary;
import java.util.UUID;
import Model.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    Dictionary<String,AuthData> allAuthDataStorage;

    @Override
    public void createAuth() {

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
