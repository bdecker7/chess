package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameSQL implements GameDAO{
    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public boolean checkIfGameExists(int gameID) {
        return false;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(ChessGame.TeamColor colorToUpdate, String usernameToInput, int gameID) {

    }

    @Override
    public void clear() {

    }

    @Override
    public HashMap<Integer, GameData> getGameHash() {
        return null;
    }
}
