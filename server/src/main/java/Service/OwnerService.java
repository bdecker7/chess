package Service;

import Handler.DeleteRequest;
import dataaccess.*;
import org.eclipse.jetty.client.api.Result;
import spark.Request;

public class OwnerService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public OwnerService(UserDAO userDAO,AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public String deleteAllDatabase(DeleteRequest deleteRequest) throws RuntimeException{
        try{
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            return null;
        } catch (RuntimeException e) {
            //not sure how to do this one.
            throw new RuntimeException("Error: Couldn't clear databases");
        }
    }
}
