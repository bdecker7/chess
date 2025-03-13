package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.SQLERROR;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthDAOTest {
    static String username = "Bill";
    static String password = "myPassword";
    static String email = "bill.nye@gmail.com";
    UserData userData = new UserData(username, password, email);

    UserDAO userDAO;
    AuthDAO authDAO;

    public AuthDAOTest() throws SQLException, DataAccessException {
        this.userDAO = new UserSQL();
        this.authDAO = new AuthSQL();
    }

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        userDAO.clear();
        userDAO.createUser(userData);
        authDAO.clear();
        authDAO.createAuth(username);
    }

    @Test
    void createAuthSuccess() throws SQLException {
        authDAO.clear();
        String authString = authDAO.createAuth(username);
        Assertions.assertNotNull(authString);
    }


    @Test
    void getAuthUsernameSuccess() throws DataAccessException, SQLException {
        authDAO.clear();
        String authString = authDAO.createAuth(username);
        Assertions.assertEquals(username,authDAO.getAuthUsername(authString));

    }

    @Test
    void getAuthUsernameFail() throws SQLException, DataAccessException {
        authDAO.clear();
        String authString = authDAO.createAuth("notUserName");
        Assertions.assertNotEquals(username,authDAO.getAuthUsername(authString));

    }

    @Test
    void deleteAuthSuccess() throws SQLException {
        authDAO.clear();
        String authString = authDAO.createAuth(username);
        authDAO.deleteAuth(authString);
        Assertions.assertFalse(authDAO.authTokenExists(authString));
    }

    @Test
    void deleteAuthFail() throws SQLException {

    }

    @Test
    void clearSuccess() throws SQLException {
        authDAO.clear();
        ResultSet rs;
        var statement = "SELECT * FROM authData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                rs = ps.executeQuery();
                Assertions.assertFalse(rs.next(), "Table should be empty after clear");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearFail() throws SQLException, DataAccessException {
        authDAO.clear();
        authDAO.createAuth(username);
        ResultSet rs;
        var statement = "SELECT * FROM authData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                rs = ps.executeQuery();
                Assertions.assertTrue(rs.next(), "Table should not be empty after recreating auth");
            }
        }
    }

    @Test
    void authTokenExistsSuccess() throws SQLException {
        authDAO.clear();
        String authString = authDAO.createAuth(username);
        boolean isAuth = authDAO.authTokenExists(authString);
        Assertions.assertTrue(isAuth);
    }

    @Test
    void authTokenExistsFail(){
        authDAO.clear();
        boolean isAuth = authDAO.authTokenExists("notValidAuth");
        Assertions.assertFalse(isAuth);
    }


}
