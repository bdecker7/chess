package dataaccess;

import Model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    HashMap<String,UserData> allUsersDataStorage;

    public MemoryUserDAO(){
        this.allUsersDataStorage= new HashMap<>();
    }

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
