package dataaccess;

import java.util.HashMap;

public class AuthSQL implements AuthDAO{
    @Override
    public String createAuth(String currentUser) {
        return "";
    }

    @Override
    public String getAuthUsername(String auth) {
        return "";
    }

    @Override
    public void deleteAuth(String auth) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean usernameInAuthDatabase(String authToken) {
        return false;
    }

    @Override
    public HashMap<String, String> grabHash() {
        return null;
    }

    @Override
    public boolean authTokenExists(String authToken) {
        return false;
    }

    @Override
    public HashMap<String, String> getAuthHash() {
        return null;
    }
}
