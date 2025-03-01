package service;

import Handler.RegisterRequest;
import Service.GameService;
import Service.RegisterResult;
import Service.UserService;
import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    static String username;
    static String password;
    static String email;
    static UserDAO dataOfUser;
    static AuthDAO authData;
    static RegisterRequest request;
    static UserService newService;
    static RegisterResult validUser;
    static GameService newGameService;

    @BeforeAll
    static void setUp() throws DataAccessException, AlreadyTakenException {
        username = "Bill";
        password = "myPassword";
        email = "bill.nye@gmail.com";

        dataOfUser = new MemoryUserDAO();
        authData = new MemoryAuthDAO();

        request = new RegisterRequest(username,password,email);
        newService = new UserService(dataOfUser,authData);
        validUser = newService.register(request);

    }
    @AfterEach
    void clearData(){
        dataOfUser.clear();
        authData.clear();
    }
    @Test
    void getListOfGames() {


    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void checkPlayerColor() {
    }
}