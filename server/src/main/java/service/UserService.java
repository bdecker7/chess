package service;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnAuthorizedException;
import model.AuthData;
import model.UserData;
import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;
import records.LoginRequest;
import records.LoginResult;
import records.RegisterRequest;
import records.RegisterResult;

import java.sql.SQLException;
import java.util.Objects;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO,AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
//This is where the password hashing should be used.

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException, SQLException {

        //checks if username is already taken
        if(userDAO.checkUser(registerRequest.username())){
            throw new AlreadyTakenException("Error: Username already taken");
        }
        //if any of the data in register request is null, I should throw an exception.
        //currently not working right now
        else if(registerRequest.username() == null
                || registerRequest.password() == null
                || registerRequest.email() == null){
            throw new DataAccessException("Error: Bad request. Make sure username, password, or email is not empty.");
        }
        //creates hashed password of current password
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        //creates a new user
        UserData newUser = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
        //puts new user in the database
        userDAO.createUser(newUser);
        //creates an authorization Token for the new user
        String newAuthToken = authDAO.createAuth(newUser.username());
        return new RegisterResult(registerRequest.username(), newAuthToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws SQLException, DataAccessException {
        var userPasswordHashed = userDAO.getUserData(loginRequest.username()).password();
        boolean is = BCrypt.checkpw(loginRequest.password(), userDAO.getUserData(loginRequest.username()).password());
        if(!userDAO.checkUser(loginRequest.username())){
            throw new UnAuthorizedException("Error: Not Valid Username");
        } else if (!BCrypt.checkpw(loginRequest.password(), userDAO.getUserData(loginRequest.username()).password())){
            throw new UnAuthorizedException("Error: Not Authorized");
        }
        AuthData newAuthTokenAssignment = new AuthData(loginRequest.username());
        String newAuthToken = authDAO.createAuth(newAuthTokenAssignment.username());
        return new LoginResult(newAuthTokenAssignment.username(), newAuthToken);
    }
    public void logout(String authString) throws SQLException {
        if(!authDAO.authTokenExists(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized logout. Check Auth Token.");
        }
        authDAO.deleteAuth(authString);
    }

}
