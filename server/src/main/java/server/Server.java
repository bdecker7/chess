package server;

import dataaccess.exceptions.DataAccessException;
import handler.*;
import dataaccess.*;
import handler.LogOutHandler;
import spark.*;
import java.sql.*;
import dataaccess.DatabaseManager.*;

import javax.xml.crypto.Data;

import static java.sql.DriverManager.getConnection;


public class Server {
    WebSocketRequestHandler websocket;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        try{
            UserDAO usersMemory = new UserSQL();
            AuthDAO authsMemory = new AuthSQL();
            GameDAO gamesMemory = new GameSQL();

            websocket = new WebSocketRequestHandler(usersMemory,authsMemory,gamesMemory);
            Spark.webSocket("/ws", websocket);

        Spark.post("/user", (req, res) ->
                new RegisterHandler(usersMemory,authsMemory).handleRequest(req,res));
        Spark.post("/session",(req,res)->
                new LoginHandler(usersMemory,authsMemory).handleLoginRequest(req,res));
        Spark.delete("/session",((request, response) ->
                new LogOutHandler(usersMemory,authsMemory).handleLogOutRequest(request,response)));
        Spark.post("/game",((request, response) ->
                new GameHandler(gamesMemory,authsMemory).handleRequest(request,response)));
        Spark.delete("/db",((request, response) ->
                new DeleteEverything(usersMemory,authsMemory,gamesMemory).deleteAllData(request,response)));
        Spark.put("/game",((request, response) ->
                new GameHandler(gamesMemory,authsMemory).handleJoinRequest(request,response)));
        Spark.get("/game",((request, response) ->
                new GameHandler(gamesMemory,authsMemory).handleListRequest(request,response)));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }catch (SQLException|DataAccessException e){
        throw new RuntimeException("Can't access data");
    }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
