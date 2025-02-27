package Service;
import Handler.*;
import Model.AuthData;
import Model.GameData;
import dataaccess.*;

import java.util.Objects;

public class GameService {

    AuthDAO authToken;
    GameDAO gameData;

    public GameService(AuthDAO authToken, GameDAO gameData){
        this.authToken = authToken;
        this.gameData = gameData;
    }

    public ListGameResult getListOfGames(ListGameRequest listOfGamesRequest)throws UnAuthorizedException, DataAccessException {
        return null;
    }

    public CreateGameResult createGame(String authString, CreateGameRequest createGameRequest)throws UnAuthorizedException, DataAccessException{
        if(!authToken.usernameInAuthDatabase(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized");
        }else if(createGameRequest.gameName() == null){
            throw new DataAccessException("Error: Incorrect Body Format");
        }
        GameData currentGameData = gameData.createGame(createGameRequest.gameName());
        return new CreateGameResult(currentGameData.gameID());
    }
    public void joinGame(String authString, joinGameRequest joinGamesRequest) throws AlreadyTakenException,UnAuthorizedException,ServerMalfunctionException,DataAccessException {
        if(!authToken.usernameInAuthDatabase(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized");
        }else if(gameData.getGame(joinGamesRequest.gameID())){
            throw new DataAccessException("Error: Incorrect Body Format");
        }
        GameData currentGameData = gameData.createGame(createGameRequest.gameName());
        return new CreateGameResult(currentGameData.gameID());
    }

}
