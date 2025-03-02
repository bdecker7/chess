package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName);
    boolean checkIfGameExists(int gameID);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(ChessGame.TeamColor colorToUpdate,String usernameToInput,int gameID);
    void clear();
    HashMap<Integer, GameData> getGameHash();

}
