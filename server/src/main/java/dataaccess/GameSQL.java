package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.InvalidColorException;
import dataaccess.exceptions.SQLERROR;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameSQL implements GameDAO{

    public GameSQL() throws SQLException, DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData createGame(String gameName) throws SQLException {
        Random random = new Random();
        int newGameID = 1000 + random.nextInt(9000);
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(newGameID,null,null,gameName,game);

        try(var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, gameObject) VALUES (? , ? , ?, ?, ?)")) {
                preparedStatement.setInt(1, newGameID);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3,null);
                preparedStatement.setString(4,gameName);

                // Serialize and store the gameDataObject as JSON.
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
        //calls execute query function
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
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(ChessGame.TeamColor colorToUpdate, String usernameToInput, int gameID) throws SQLException, InvalidColorException {

        String statement;
        if(colorToUpdate == ChessGame.TeamColor.WHITE){
            statement = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";
        }else if(colorToUpdate == ChessGame.TeamColor.BLACK){
            statement = "UPDATE gameData SET blackUsername = ?, gameObject = ?  WHERE gameID = ?";
        }
        else{
            throw new InvalidColorException("Invalid color");
        }
        executeUpdate(statement,usernameToInput, gameID);
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

    @Override
    public HashMap<Integer, GameData> getGameHash() {
        return null;
    }

    private void executeUpdate(String statement, Object... params) throws SQLException, SQLERROR {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new SQLERROR(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

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


    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new SQLERROR(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
