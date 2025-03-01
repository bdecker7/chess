package service;

import records.CreateGameRequest;
import records.JoinGameRequest;
import records.RegisterRequest;
import records.RegisterResult;
import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    static void setUp() throws DataAccessException, AlreadyTakenException {
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
        dataOfUser.clear();
        authData.clear();

    }
    @Test
    void getListOfGames() throws DataAccessException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "GAME 1");
        newGameService.createGame(validUser.authToken(), createdGame);
        CreateGameRequest otherCreatedGame = new CreateGameRequest(validUser.authToken(), "GAME 2");
        newGameService.createGame(validUser.authToken(), otherCreatedGame);

        Assertions.assertTrue(gameDataOfUser.getGameHash().size() == 2);

    }

    @Test
    void getListOfGamesFail() throws DataAccessException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "GAME 1");
        newGameService.createGame(validUser.authToken(), createdGame);
        CreateGameRequest otherCreatedGame = new CreateGameRequest(validUser.authToken(), "GAME 2");
        newGameService.createGame(validUser.authToken(), otherCreatedGame);

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newGameService.getListOfGames("notValidAuthToKEN")
        );


    }

    @Test
    void createGame() throws DataAccessException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "NEW GAME");
        newGameService.createGame(validUser.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUser.authToken(), createdGame).gameID();
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
    void joinGame() throws DataAccessException, AlreadyTakenException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "NEW GAME");
        newGameService.createGame(validUser.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUser.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);
        newGameService.joinGame(validUser.authToken(), joinRequest);

        Assertions.assertTrue(Objects.equals(gameDataOfUser.getGame(testedGameID).whiteUsername(), "Bill"));
    }
    @Test
    void joinGameFail() throws DataAccessException, AlreadyTakenException {
        CreateGameRequest createdGame = new CreateGameRequest(validUser.authToken(), "NEW GAME");
        newGameService.createGame(validUser.authToken(), createdGame);
        int testedGameID = newGameService.createGame(validUser.authToken(), createdGame).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(WHITE, testedGameID);

        UnAuthorizedException thrown = assertThrows(
                UnAuthorizedException.class,
                () -> newGameService.joinGame("notValidAUTHTOKEN", joinRequest)
        );
    }
}