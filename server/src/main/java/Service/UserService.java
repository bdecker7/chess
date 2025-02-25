package Service;
import Handler.*;
import Model.UserData;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;

public class UserService {

    MemoryUserDAO userDAO;
    MemoryAuthDAO authDAO;

    public UserService(){
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException {

        //checks if username is already taken
        if(userDAO.checkUser(registerRequest.username())){
            throw new AlreadyTakenException("Error: Username already taken");
        }
        //if any of the data in register request is null, I should throw an exception.
        else if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            throw new DataAccessException("Error: Bad request");
        }
        //creates a new user
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        //puts new user in the database
        userDAO.createUser(newUser);
        //creates an authorization Token for the new user
        authDAO.createAuth(newUser);
        String newUserAuthToken = authDAO.getAuth(registerRequest.username());
        RegisterResult newRegisterer = new RegisterResult(registerRequest.username(), newUserAuthToken);
        return newRegisterer;
    }

    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogOutRequest logoutRequest) {}

}
