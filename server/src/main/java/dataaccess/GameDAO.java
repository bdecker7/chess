package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;
import chess.ChessGame;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName) throws SQLException;
    boolean checkIfGameExists(int gameID);
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(ChessGame.TeamColor colorToUpdate,String usernameToInput,int gameID) throws SQLException;
    void clear();
    void updateAfterMove(int gameID, ChessGame game) throws SQLException;

}
