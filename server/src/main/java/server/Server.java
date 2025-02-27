package server;

import Handler.*;
import dataaccess.*;
import spark.*;
import Service.OwnerService;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        UserDAO UsersMemory = new MemoryUserDAO();
        AuthDAO AuthsMemory = new MemoryAuthDAO();
        GameDAO GamesMemory = new MemoryGameDAO();

        //does registering also log in a user?
        Spark.post("/user", (req, res) ->
                new RegisterHandler(UsersMemory,AuthsMemory).handleRequest(req,res));
        Spark.post("/session",(req,res)->
                new LoginHandler(UsersMemory,AuthsMemory).handleLoginRequest(req,res));
        Spark.delete("/session",((request, response) ->
                new LogOutHandler(UsersMemory,AuthsMemory).handleLogOutRequest(request,response)));
        Spark.post("/game",((request, response) ->
                new GameHandler(GamesMemory,AuthsMemory).handleRequest(request,response)));
        Spark.delete("/db",((request, response) ->
                new deleteEverything(UsersMemory,AuthsMemory,GamesMemory).deleteAllData(request,response)));

        // Works, but username can be black and white player. Probably not what I want to happen.
        Spark.put("/game",((request, response) ->
                new GameHandler(GamesMemory,AuthsMemory).handleJoinRequest(request,response)));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
