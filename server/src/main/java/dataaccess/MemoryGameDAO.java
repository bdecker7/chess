package dataaccess;

import Model.GameData;
import Model.UserData;
import chess.ChessGame;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    HashMap<Integer, GameData> allGameDataStorage;

    public MemoryGameDAO(){
        this.allGameDataStorage= new HashMap<>();
    }


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

    @Override
    public void clear() {
        allGameDataStorage.clear();
    }
}
