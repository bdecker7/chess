package dataaccess;

//clear: A method for clearing all data from the database. This is used during testing.
//createUser: Create a new user.
//getUser: Retrieve a user with the given username.

import dataaccess.exceptions.DataAccessException;
import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;

public interface UserDAO {

    void clear() throws SQLException, DataAccessException;
    void createUser(UserData data) throws SQLException;
    boolean checkUser(String username) throws SQLException;
    UserData getUserData(String username) throws DataAccessException, SQLException;
}
