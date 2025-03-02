package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{

    private HashMap<Integer, GameData> allGameDataStorage;

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
    public Collection<GameData> listGames() {
        return allGameDataStorage.values();
    }

    @Override
    public void updateGame(ChessGame.TeamColor colorToUpdate, String usernameToInput, int gameID) {
        GameData currentData = allGameDataStorage.get(gameID);
        if(colorToUpdate == ChessGame.TeamColor.WHITE){
            GameData newGameData =
                    new GameData(gameID,usernameToInput, currentData.blackUsername(), currentData.gameName(),currentData.game());
            allGameDataStorage.replace(gameID,newGameData);
        }else if(colorToUpdate == ChessGame.TeamColor.BLACK){
            GameData newGameData =
                    new GameData(gameID, currentData.whiteUsername(), usernameToInput, currentData.gameName(),currentData.game());
            allGameDataStorage.replace(gameID,newGameData);
        }
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
