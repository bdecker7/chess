package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.ServerMalfunctionException;
import dataaccess.UserDAO;
import spark.Request;
import spark.Response;

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
        if(!authDAO.grabHash().isEmpty()){
            throw new ServerMalfunctionException("Error: AuthData was not emptied");
        }

    }
    private void deleteUser(){
        userDAO.clear();
        if(!userDAO.getUserHashMap().isEmpty()){
            throw new ServerMalfunctionException("Error: UserData was not emptied");
        }
    }
    private void deleteGame(){
        gameDAO.clear();
        if(!gameDAO.getGameHash().isEmpty()){
            throw new ServerMalfunctionException("Error: GameData was not emptied");
        }
    }

    public String deleteAllData(Request req, Response res){
        try{
            deleteAuth();
            deleteGame();
            deleteUser();
            res.status(200);
            return "{}";

        }catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }

}

