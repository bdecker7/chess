package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;

public class MemoryGameDAO implements GameDAO{

//    ArrayList<MemoryGameDAO> allGameDAOs; //possibly store in a list or dictionary by game id
    Dictionary<Integer,GameData> allGameDAOs;


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
