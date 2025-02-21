package Service;
import Handler.*;
import Model.UserData;
import dataaccess.MemoryUserDAO;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
        getUser(registerRequest.username());
        return null;}

    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogOutRequest logoutRequest) {}

    public UserData getUser(String username){
//        if(MemoryUserDAO.contains(username) != false){
//
//        }
    }
}
