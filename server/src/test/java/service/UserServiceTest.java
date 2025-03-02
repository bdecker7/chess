package service;

import records.LoginRequest;
import records.RegisterRequest;
import records.LoginResult;
import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static String username;
    static String password;
    static String email;
    static UserDAO dataOfUser;
    static AuthDAO authData;
    static RegisterRequest request;
    static UserService newService;



    @BeforeAll
    static void setUp(){
        username = "Bill";
        password = "myPassword";
        email = "bill.nye@gmail.com";

        dataOfUser = new MemoryUserDAO();
        authData = new MemoryAuthDAO();

        request = new RegisterRequest(username,password,email);
        newService = new UserService(dataOfUser,authData);
    }
    @AfterEach
    void clearData(){
        dataOfUser.clear();
        authData.clear();
    }

    @Test
    void registerFail() throws AlreadyTakenException, DataAccessException {
        //register user
        //register user again
        //assert already taken exception

        newService.register(request);

        RegisterRequest requestTest = new RegisterRequest(username, password,email);
//        newService.register(requestTest);

        AlreadyTakenException thrown = assertThrows(
                AlreadyTakenException.class,
                () -> newService.register(requestTest)
        );
    }
    @Test
    void registerSuccess() throws AlreadyTakenException, DataAccessException {
        //register user
        RegisterRequest requestOther =
                new RegisterRequest("Bill","myPassword","bill.nye@gmail.com");
        Assertions.assertEquals(newService.register(requestOther).username(),"Bill");

    }


    @Test
    void loginSuccess() throws DataAccessException, AlreadyTakenException {
        //register user
        newService.register(request);

        //Login User
        LoginRequest loginOther = new LoginRequest("Bill","myPassword");
        Assertions.assertEquals(newService.login(loginOther).username(),username);
    }
    @Test
    void loginFail() throws DataAccessException, AlreadyTakenException {
        //Login User W/out valid username
        //assert Data access exception
        newService.register(request);

        LoginRequest loginOther = new LoginRequest("DIFF Username","myPassword");

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newService.login(loginOther)
        );
    }

    @Test
    void logoutSuccess() throws DataAccessException, AlreadyTakenException {
        //register user
        newService.register(request);
        //Login User
        LoginRequest loginOther = new LoginRequest("Bill","myPassword");
        LoginResult loggedIn = newService.login(loginOther);
        //logout user
        newService.logout(loggedIn.authToken());
        Assertions.assertEquals(1, authData.getAuthHash().size());

    }

    @Test
    void logoutFail() throws UnAuthorizedException, DataAccessException, AlreadyTakenException {
        //register user
        newService.register(request);
        //Login User
        LoginRequest loginOther = new LoginRequest("Bill","myPassword");
        LoginResult loggedIn = newService.login(loginOther);
        //logout user
        LoginResult diffAuthToken = new LoginResult("Bill", "bad Auth");
        newService.logout(loggedIn.authToken());

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newService.logout(diffAuthToken.authToken())
        );
    }
}