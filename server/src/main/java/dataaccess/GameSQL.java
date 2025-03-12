package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.InvalidColorException;
import dataaccess.exceptions.SQLERROR;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameSQL implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` int(4) NOT NULL,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `gameName` varchar(256) NOT NULL,
              `gameObject` longtext NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };
    public GameSQL() throws SQLException, DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public GameData createGame(String gameName) throws SQLException {
        Random random = new Random();
        int newGameID = 1000 + random.nextInt(9000);
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(newGameID,null,null,gameName,game);

        try(var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, gameObject) " +
                            "VALUES (? , ? , ?, ?, ?)")) {
                preparedStatement.setInt(1, newGameID);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3,null);
                preparedStatement.setString(4,gameName);

                var gameDataJson = new Gson().toJson(game);
                preparedStatement.setString(5, gameDataJson);

                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return newGameData;
    }

    @Override
    public boolean checkIfGameExists(int gameID) {

        ResultSet rs;
        var statement = "SELECT gameID FROM gameData WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1,gameID);
                rs = ps.executeQuery();
                if(rs.next()){
                    return Objects.equals(rs.getInt("gameID"), gameID);
                }else{
                    return false;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {

        ResultSet rs;
        var statement = "SELECT * FROM gameData WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1,gameID);
                rs = ps.executeQuery();
                if(rs.next()){
                    Integer gameIDNumber = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String jsonGameData = rs.getString("gameObject");
                    var gameDataObject = new Gson().fromJson(jsonGameData,ChessGame.class);
                    return new GameData(gameIDNumber,whiteUsername,blackUsername,gameName,gameDataObject);
                }else{
                    throw new DataAccessException("can't access data");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch(DataAccessException e){
            throw new DataAccessException("couldn't get user data");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {

        ArrayList<GameData> allGames = new ArrayList<GameData>();

        ResultSet rs;
        var statement = "SELECT * FROM gameData";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                rs = ps.executeQuery();
                while(rs.next()){
                    Integer gameIDNumber = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String jsonGameData = rs.getString("gameObject");
                    var gameDataObject = new Gson().fromJson(jsonGameData,ChessGame.class);
                    GameData individualGameData = new GameData(gameIDNumber,whiteUsername,blackUsername,gameName,gameDataObject);
                    allGames.add(individualGameData);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch(DataAccessException e){
            throw new DataAccessException("couldn't get user data");
        }

        return allGames;
    }

    @Override
    public void updateGame(ChessGame.TeamColor colorToUpdate, String usernameToInput, int gameID) throws SQLException, InvalidColorException {

        String statement;
        if(colorToUpdate == ChessGame.TeamColor.WHITE){
            statement = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";
        }else if(colorToUpdate == ChessGame.TeamColor.BLACK){
            statement = "UPDATE gameData SET blackUsername = ?  WHERE gameID = ?";
        }
        else{
            throw new InvalidColorException("Invalid color");
        }
        DatabaseManager.executeUpdate(statement,usernameToInput, gameID);
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE gameData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new SQLERROR(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
