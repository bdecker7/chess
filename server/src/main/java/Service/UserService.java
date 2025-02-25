package Service;
import Handler.*;
import Model.UserData;
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

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        //if any of the data in register request is null, I should throw an exception.
        //use different if statements for that.
        // make more Exceptions
        if(userDAO.checkUser(registerRequest.username())){

        }
        //checks if username is already taken
        MemoryAuthDAO authToken = new authDAO.createAuth(registerRequest.username());
        RegisterResult newRegisterer = new RegisterResult(registerRequest.username(), authToken);
        return newRegisterer;
    }

    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogOutRequest logoutRequest) {}

}
