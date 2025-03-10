package service;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnAuthorizedException;
import records.*;
import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    static String username;
    static String password;
    static String email;
    static UserDAO dataOfUser;
    static AuthDAO authData;
    static GameDAO gameDataOfUser;
    static RegisterRequest request;
    static UserService newService;
    static RegisterResult validUser;
    static GameService newGameService;

    @BeforeAll
    static void setUp() throws DataAccessException, AlreadyTakenException, SQLException {
        username = "Bill";
        password = "myPassword";
        email = "bill.nye@gmail.com";

        dataOfUser = new MemoryUserDAO();
        authData = new MemoryAuthDAO();
        gameDataOfUser = new MemoryGameDAO();

        request = new RegisterRequest(username,password,email);
        newService = new UserService(dataOfUser,authData);
        validUser = newService.register(request);
        newGameService = new GameService(authData,gameDataOfUser);

    }
    @AfterEach
    void clearData(){

        authData.clear();
        gameDataOfUser.clear();

    }
    @Test
    void getListOfGames() throws DataAccessException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "GAME 1");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        CreateGameRequest otherCreatedGame = new CreateGameRequest(validUserLogin.authToken(), "GAME 2");
        newGameService.createGame(validUserLogin.authToken(), otherCreatedGame);

        Assertions.assertTrue(gameDataOfUser.getGameHash().size() == 2);

    }

    @Test
    void getListOfGamesFail() throws DataAccessException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "GAME 1");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        CreateGameRequest otherCreatedGame = new CreateGameRequest(validUserLogin.authToken(), "GAME 2");
        newGameService.createGame(validUserLogin.authToken(), otherCreatedGame);

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newGameService.getListOfGames("notValidAuthToKEN")
        );


    }

    @Test
    void createGame() throws DataAccessException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "NEW GAME");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUserLogin.authToken(), createdGame).gameID();
        Assertions.assertTrue(testedGameID > 1000);
    }
    @Test
    void createGameFail() throws DataAccessException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "NEW GAME");

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newGameService.createGame("Not VALID AUTHTOKEN", createdGame)
        );
    }


    @Test
    void joinGame() throws DataAccessException, AlreadyTakenException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "NEW GAME");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUserLogin.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);
        newGameService.joinGame(validUserLogin.authToken(), joinRequest);

        Assertions.assertTrue(Objects.equals(gameDataOfUser.getGame(testedGameID).whiteUsername(), "Bill"));
    }
    @Test
    void joinGameFail() throws DataAccessException, AlreadyTakenException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "NEW GAME");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUserLogin.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newGameService.joinGame("notValidAUTHTOKEN", joinRequest)
        );
    }
    @Test
    void checkPlayerColorTest() throws DataAccessException, AlreadyTakenException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "NEW GAME");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUserLogin.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);

        String testedUsername = newGameService.checkPlayerColor(joinRequest.playerColor(), joinRequest.gameID(), validUserLogin.authToken());
        Assertions.assertEquals(testedUsername,username);

    }
    @Test
    void checkPlayerColorTestFailure() throws DataAccessException, AlreadyTakenException, SQLException {
        LoginRequest newLogin = new LoginRequest(username,password);
        LoginResult validUserLogin = newService.login(newLogin);
        CreateGameRequest createdGame = new CreateGameRequest(validUserLogin.authToken(), "NEW GAME");
        newGameService.createGame(validUserLogin.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUserLogin.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);
        newGameService.joinGame(validUserLogin.authToken(),joinRequest);

        AlreadyTakenException thrown = assertThrows(
                AlreadyTakenException.class,
                () -> newGameService.checkPlayerColor(joinRequest.playerColor(), joinRequest.gameID(), validUserLogin.authToken())
        );

    }
}