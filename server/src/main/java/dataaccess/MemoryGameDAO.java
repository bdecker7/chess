package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    @Override
    public GameData createGame() {
        return null;
    }

    @Override
    public ChessGame getGame(int gameID) {
        return null;
    }

    @Override
    public ArrayList<ChessGame> listGames() {
        return null;
    }

    @Override
    public void updateGame() {

    }
}
