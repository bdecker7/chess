package dataaccess;

import Model.UserData;

import java.util.Dictionary;

public class MemoryUserDAO implements UserDAO{

    Dictionary<String,UserData> allUsersDataStorage;

    @Override
    public void clear() {

    }

    @Override
    public UserData createUser() {

        return null;
    }

    @Override
    public void getUser() {

    }
}
