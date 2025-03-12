package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.SQLERROR;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthSQL extends MemoryAuthDAO implements AuthDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };

    public AuthSQL() throws SQLException, DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public String createAuth(String currentUser) throws SQLException {
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO authData (username,authToken) VALUES (?,?)";
        DatabaseManager.executeUpdate(statement,currentUser,authToken);
        return authToken;
    }

    @Override
    public String getAuthUsername(String auth) throws DataAccessException {
        //calls execute query function
        ResultSet rs;
        var statement = "SELECT * FROM authData WHERE authToken = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,auth);
                rs = ps.executeQuery();
                if(rs.next()){
                    String usernameFromSQL = rs.getString("username");
                    String authToken = rs.getString("authToken");
                    return usernameFromSQL;
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
    public void deleteAuth(String auth) throws SQLException {
        var statement = "DELETE FROM authData WHERE authToken= ?";
        DatabaseManager.executeUpdate(statement,auth);
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE authData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new SQLERROR(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

    }

    @Override
    public boolean authTokenExists(String authToken) {
        ResultSet rs;
        var statement = "SELECT authToken FROM authData WHERE authToken = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                rs = ps.executeQuery();
                if(rs.next()){
                    return Objects.equals(rs.getNString("authToken"), authToken);
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
