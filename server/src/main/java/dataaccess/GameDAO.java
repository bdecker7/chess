package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashMap;

//createGame: Create a new game.
//getGame: Retrieve a specified game with the given game ID.
//listGames: Retrieve all games.
//updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
public interface GameDAO {

    GameData createGame(String gameName);
    boolean checkIfGameExists(int gameID);
    GameData getGame(int gameID);
    ArrayList<ChessGame> listGames();
    void updateGame(); //not sure if this returns anything
    void clear();
    HashMap<Integer, GameData> getGameHash();

}
