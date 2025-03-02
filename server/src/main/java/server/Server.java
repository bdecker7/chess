package server;

import handler.*;
import dataaccess.*;
import handler.LogOutHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        UserDAO usersMemory = new MemoryUserDAO();
        AuthDAO authsMemory = new MemoryAuthDAO();
        GameDAO gamesMemory = new MemoryGameDAO();

        //does registering also log in a user?
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
        // Works, but username can be black and white player. Probably not what I want to happen.
        Spark.put("/game",((request, response) ->
                new GameHandler(gamesMemory,authsMemory).handleJoinRequest(request,response)));

        Spark.get("/game",((request, response) ->
                new GameHandler(gamesMemory,authsMemory).handleListRequest(request,response)));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
