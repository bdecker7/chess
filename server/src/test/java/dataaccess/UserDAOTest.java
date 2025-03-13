package dataaccess;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.SQLERROR;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Nested
class UserDAOTest {

    static String username = "Bill";
    static String password = "myPassword";
    static String email = "bill.nye@gmail.com";
    UserData userData = new UserData(username, password, email);

    UserDAO userDAO;

    public UserDAOTest() throws SQLException, DataAccessException {
        this.userDAO = new UserSQL();
    }

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        userDAO.clear();
        userDAO.createUser(userData);
    }

    @Test
    void clearTestSuccess() throws DataAccessException, SQLException {
        userDAO.clear();
        ResultSet rs;
        var statement = "SELECT * FROM userData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                rs = ps.executeQuery();
                Assertions.assertFalse(rs.next(), "Table should be empty after clear");
            }
        }
    }

    @Test
    void clearTestFail() throws SQLException, DataAccessException {
        userDAO.clear();
        userDAO.createUser(userData);
        ResultSet rs;
        var statement = "SELECT * FROM userData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                rs = ps.executeQuery();
                Assertions.assertTrue(rs.next(), "Table should not be empty after recreating user");
            }
        }
    }

    @Test
    void createUserSuccess() throws SQLException, DataAccessException {
        userDAO.clear();
        userDAO.createUser(userData);

        ResultSet rs;
        var statement = "SELECT username FROM userData WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                rs = ps.executeQuery();
                if (rs.next()) {
                    Assertions.assertEquals(rs.getNString("username"), username);
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
}
    @Test
    void createUserInvalid() throws SQLException, DataAccessException {
        //make user
        //try to create same username
        //throws alreadyTaken exception

        userDAO.clear();
        userDAO.createUser(userData);
        SQLERROR thrown = assertThrows(
                SQLERROR.class,
                () -> userDAO.createUser(userData)
        );
    }


    @Test
    void checkUserSuccess() throws SQLException {

        boolean isUser = userDAO.checkUser(username);
        Assertions.assertTrue(isUser);

    }

    @Test
    void checkUserFail() throws SQLException {

        boolean isUser = userDAO.checkUser("NOTgoodUsername");
        Assertions.assertFalse(isUser);

    }

    @Test
    void getUserDataSuccess() throws SQLException, DataAccessException {

        UserData userTest = userDAO.getUserData(username);
        Assertions.assertEquals(userData,userTest);

    }

    @Test
    void getUserDataFail() throws SQLException, DataAccessException {

        DataAccessException thrown = assertThrows(
                DataAccessException.class,
                () -> userDAO.getUserData("notValidUsername")
        );

    }
}
