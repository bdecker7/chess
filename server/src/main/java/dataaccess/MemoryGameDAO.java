package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{

    HashMap<Integer, GameData> allGameDataStorage;

    public MemoryGameDAO(){
        this.allGameDataStorage= new HashMap<>();
    }


    @Override
    public GameData createGame(String gameName) {
        Random random = new Random();
        int newGameID = 1000 + random.nextInt(9000);
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(newGameID,null,null,gameName,game);
        allGameDataStorage.put(newGameID,newGameData);
        return newGameData;
    }

    @Override
    public boolean checkIfGameExists(int gameID) {
        return allGameDataStorage.containsKey(gameID);
    }

    @Override
    public GameData getGame(int gameID) {
        return allGameDataStorage.get(gameID);
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

    @Override
    public HashMap<Integer, GameData> getGameHash() {
        return allGameDataStorage;
    }
}
