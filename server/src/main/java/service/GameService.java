package service;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ServerMalfunctionException;
import dataaccess.exceptions.UnAuthorizedException;
import model.GameData;
import chess.ChessGame;
import dataaccess.*;
import records.*;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {

    AuthDAO authToken;
    GameDAO gameData;

    public GameService(AuthDAO authToken, GameDAO gameData){
        this.authToken = authToken;
        this.gameData = gameData;
    }

    public ListGameResult getListOfGames(String authString)
            throws UnAuthorizedException, ServerMalfunctionException {
        if(!authToken.authTokenExists(authString)){
            throw new UnAuthorizedException("Error: not authorized");
        }else if(authToken.authTokenExists(authString)){
            Collection<GameData> allGameData = gameData.listGames();
            ArrayList<SingleListedGame> gamesResultDataList = new ArrayList<SingleListedGame>();
            for(GameData data: allGameData){
                SingleListedGame gameListSingle =
                        new SingleListedGame(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName());
                gamesResultDataList.add(gameListSingle);
            }
            return new ListGameResult(gamesResultDataList);
        }else{
            throw new ServerMalfunctionException("Error: Server data error");
        }
    }

    public CreateGameResult createGame(String authString, CreateGameRequest createGameRequest)
            throws UnAuthorizedException, DataAccessException {
        if(!authToken.usernameInAuthDatabase(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized");
        }else if(createGameRequest.gameName() == null){
            throw new DataAccessException("Error: Incorrect Body Format");
        }
        GameData currentGameData = gameData.createGame(createGameRequest.gameName());
        return new CreateGameResult(currentGameData.gameID());
    }
    public void joinGame(String authString, JoinGameRequest joinGamesRequest)
            throws AlreadyTakenException,UnAuthorizedException,ServerMalfunctionException,DataAccessException {
        if(!authToken.usernameInAuthDatabase(authString)){
            throw new UnAuthorizedException("Error: UnAuthorized");
        }else if(authString == null || joinGamesRequest.playerColor() == null){
            throw new DataAccessException("Error: invalid input");
        }
        else if(!gameData.checkIfGameExists(joinGamesRequest.gameID())){
            throw new DataAccessException("Error: game doesn't exist");
        }
        else {
            String username = checkPlayerColor(joinGamesRequest.playerColor(), joinGamesRequest.gameID(),authString);
            gameData.updateGame(joinGamesRequest.playerColor(),username, joinGamesRequest.gameID());
        }

    }

    String checkPlayerColor(ChessGame.TeamColor requestedColor, int gameID, String authString)
            throws AlreadyTakenException, DataAccessException {
        if(requestedColor == ChessGame.TeamColor.WHITE){
            if(gameData.getGame(gameID).whiteUsername() != null) {
                throw new AlreadyTakenException("Error: White already taken");
            }else{
                return authToken.getAuthUsername(authString);
            }

        }else if(requestedColor == ChessGame.TeamColor.BLACK){
            if(gameData.getGame(gameID).blackUsername() != null){
                throw new AlreadyTakenException("Error: Black already taken");
            }else{
                return authToken.getAuthUsername(authString);
            }
        }else{
            throw new DataAccessException("Error: bad Request");
        }
    }

}
