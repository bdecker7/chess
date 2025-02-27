package Handler;

import Service.*;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

public class GameHandler {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public String handleRequest(Request req, Response res) throws DataAccessException, UnAuthorizedException,ServerMalfunctionException {

        try {
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String authToken = new Gson().fromJson(req.headers("authorization"),String.class);
            GameService service = new GameService(authDAO, gameDAO);
            CreateGameResult result = service.createGame(authToken, request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException e) {
            res.status(400);
            return new Gson().toJson(e.getMessage());
        } catch (UnAuthorizedException e) {
            res.status(401);
            return new Gson().toJson(e.getMessage());
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }
    public String handleJoinRequest(Request req, Response res) {
        try {
            joinGameRequest request = new Gson().fromJson(req.body(), joinGameRequest.class);   //this needs to change to get the authToken parameter not the body
            String authToken = new Gson().fromJson(req.headers("authorization"),String.class);
            GameService service = new GameService(authDAO, gameDAO);
            service.joinGame(authToken, request);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(400);
            return new Gson().toJson(e.getMessage());
        } catch (AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson(e.getMessage());
        }catch (UnAuthorizedException e) {
            res.status(401);
            return new Gson().toJson(e.getMessage());
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }
    public String handleListRequest(Request req, Response res){
        try {
            String authToken = new Gson().fromJson(req.headers("authorization"),String.class);
            GameService service = new GameService(authDAO, gameDAO);
            ListGameResult result = service.getListOfGames(authToken);
            res.status(200);
            return new Gson().toJson(result);
        }  catch (UnAuthorizedException e) {
            res.status(401);
            return new Gson().toJson(e.getMessage());
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }
}
