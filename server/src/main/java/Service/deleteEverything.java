package Service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class deleteEverything {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public deleteEverything(UserDAO userDAO,AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    private void deleteAuth(){
        authDAO.clear();
    }
    private void deleteUser(){
        userDAO.clear();
    }
    private void deleteGame(){
        gameDAO.clear();
    }

}
