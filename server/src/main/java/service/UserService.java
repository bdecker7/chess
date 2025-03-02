package service;
import model.AuthData;
import model.UserData;
import dataaccess.*;
import records.LoginRequest;
import records.LoginResult;
import records.RegisterRequest;
import records.RegisterResult;

import java.util.Objects;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO,AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException {

        //checks if username is already taken
        if(userDAO.checkUser(registerRequest.username())){
            throw new AlreadyTakenException("Error: Username already taken");
        }
        //if any of the data in register request is null, I should throw an exception.
        //currently not working right now
        else if(registerRequest.username() == null
                || registerRequest.password() == null
                || registerRequest.email() == null){ //I dont think this works?
            throw new DataAccessException("Error: Bad request. Make sure username, password, or email is not empty.");
        }
        //creates a new user
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        //puts new user in the database
        userDAO.createUser(newUser);
        //creates an authorization Token for the new user
        String newAuthToken = authDAO.createAuth(newUser.username());
        return new RegisterResult(registerRequest.username(), newAuthToken);
    }

    public LoginResult login(LoginRequest loginRequest) {

        if(!userDAO.checkUser(loginRequest.username())){
            throw new UnAuthorizedException("Error: Not Valid Username");
        }else if(!Objects.equals(userDAO.getUserData(loginRequest.username()).password(), loginRequest.password())){
            throw new UnAuthorizedException("Error: Not Authorized");
        }
        AuthData newAuthTokenAssignment = new AuthData(loginRequest.username());
        String newAuthToken = authDAO.createAuth(newAuthTokenAssignment.username());
        return new LoginResult(newAuthTokenAssignment.username(), newAuthToken);
    }
    public void logout(String authString) {
        if(!authDAO.authTokenExists(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized logout. Check Auth Token.");
        }
        authDAO.deleteAuth(authString);
    }

}
