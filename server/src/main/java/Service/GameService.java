package Service;
import Handler.*;
import Model.AuthData;
import Model.GameData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnAuthorizedException;

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

    public CreateGameResult createGame(CreateGameRequest createGameRequest)throws UnAuthorizedException, DataAccessException{
        if(!authToken.usernameInAuthDatabase(createGameRequest.authToken())){
            throw new UnAuthorizedException("Error: UnAuthorized");
        }else if(Objects.equals(createGameRequest.gameName(), "")){
            throw new UnAuthorizedException("Error: Not Authorized");
        }
        GameData currentGameData = gameData.createGame(createGameRequest.gameName());
        return new CreateGameResult(currentGameData.gameID());
    }
    public void joinGame(joinGameRequest joinGamesRequest){

    }

}
