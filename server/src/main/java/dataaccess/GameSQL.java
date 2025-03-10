package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.SQLERROR;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, gameObject) VALUES (? , ? , ?, ?, ?)";
        executeUpdate(statement,newGameID,newGameData.whiteUsername(),newGameData.blackUsername(),newGameData);
        return newGameData;
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

    private void executeUpdate(String statement, Object... params) throws SQLException, SQLERROR {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
// I dont think I need this because we are not indexing.
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }

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
              `gameObject` varchar(256) NOT NULL,
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
