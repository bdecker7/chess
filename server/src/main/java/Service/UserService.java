package Service;
import Handler.*;
import Model.UserData;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;

public class UserService {

    MemoryUserDAO userDAO;
    MemoryAuthDAO authDAO;

    public UserService(){
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
    }

    public RegisterResult register(RegisterRequest registerRequest) {

        getUser(registerRequest.username());    //checks if username is already taken

        RegisterResult newRegisterer = new RegisterResult(registerRequest.username(), authDAO.getAuth());
        return newRegisterer;
    }

    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogOutRequest logoutRequest) {}

    public UserData getUser(String username){
//        if(MemoryUserDAO.contains(username) != false){
//
//        }
    }
}
