package handler;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ServerMalfunctionException;
import dataaccess.exceptions.UnAuthorizedException;
import records.*;
import service.*;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class GameHandler {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public String handleRequest(Request req, Response res)
            throws UnAuthorizedException, ServerMalfunctionException {

        try {
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("authorization");
            GameService service = new GameService(authDAO, gameDAO);
            CreateGameResult result = service.createGame(authToken, request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException e) {
            res.status(400);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        } catch (UnAuthorizedException e) {
            res.status(401);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(Error e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String handleJoinRequest(Request req, Response res) {
        try {
            JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
            String authToken = req.headers("authorization");
            GameService service = new GameService(authDAO, gameDAO);
            service.joinGame(authToken, request);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(400);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        } catch (AlreadyTakenException e) {
            res.status(403);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }catch (UnAuthorizedException e) {
            res.status(401);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(Error e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
    }
    public String handleListRequest(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            GameService service = new GameService(authDAO, gameDAO);
            ListGameResult result = service.getListOfGames(authToken);
            res.status(200);
            return new Gson().toJson(result);
        }  catch (UnAuthorizedException e) {
            res.status(401);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
    }
}
