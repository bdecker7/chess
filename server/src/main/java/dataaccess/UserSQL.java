package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.SQLERROR;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserSQL implements UserDAO{

    public UserSQL() throws SQLException, DataAccessException {
        configureDatabase();
    }


    @Override
    public HashMap<String, UserData> getUserHashMap() {
        return null;
    }

    @Override
    public void clear() throws SQLException, DataAccessException {
        var statement = "TRUNCATE TABLE userData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new SQLERROR(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void createUser(UserData data) throws SQLException {
        //this calls executeUpdate()
        var statement = "INSERT INTO userData (username, password,email) VALUES (? , ? , ?)";
        executeUpdate(statement,data.username(),data.password(),data.email());
    }

    @Override
    public boolean checkUser(String username) throws SQLException {
        //calls execute query function
        ResultSet rs;
        var statement = "SELECT username FROM userData WHERE username = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                rs = ps.executeQuery();
                if(rs.next()){
                    return Objects.equals(rs.getNString("username"), username);
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public UserData getUserData(String username) throws DataAccessException, SQLException {
        //calls execute query function
        ResultSet rs;
        var statement = "SELECT * FROM userData WHERE username = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                rs = ps.executeQuery();
                if(rs.next()){
                    String usernameFromSQL = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(usernameFromSQL,password, email);
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

    private void executeUpdate(String statement, Object... params) throws SQLException,SQLERROR {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
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
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
