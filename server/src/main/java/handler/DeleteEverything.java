package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ServerMalfunctionException;
import dataaccess.UserDAO;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class DeleteEverything {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public DeleteEverything(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private void deleteAuth() throws ServerMalfunctionException {

        authDAO.clear();

    }
    private void deleteUser() throws SQLException, DataAccessException {
        userDAO.clear();
    }
    private void deleteGame(){
        gameDAO.clear();
    }

    public String deleteAllData(Request req, Response res){
        try{
            deleteUser();
            deleteAuth();
            deleteGame();
            res.status(200);
            return "{}";

        }catch(ServerMalfunctionException | SQLException | DataAccessException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }

}

