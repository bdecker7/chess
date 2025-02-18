package dataaccess;

//clear: A method for clearing all data from the database. This is used during testing.
//createUser: Create a new user.
//getUser: Retrieve a user with the given username.

public interface UserDAO {
    void clear();
    void createUser();
    void getUser();
}
