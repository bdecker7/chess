package Handler;

import Service.CreateGameResult;
import Service.GameService;
import Service.RegisterResult;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public CreateGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public String handleRequest(Request req, Response res) throws DataAccessException, UnAuthorizedException,ServerMalfunctionException {

        try {
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);   //this needs to change to get the authToken parameter not the body
            String authToken = new Gson().fromJson(req.headers("authorization"),String.class);
            GameService service = new GameService(authDAO, gameDAO);
            CreateGameResult result = service.createGame(authToken, request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException e) {
            res.status(400);
            return new Gson().toJson(e.getMessage());
        } catch (UnAuthorizedException e) {
            res.status(403);
            return new Gson().toJson(e.getMessage());
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }
}
