package server;

import Handler.LoginHandler;
import Handler.RegisterHandler;
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
        //this is the register request
        Spark.post("/user", (req, res) ->
                new RegisterHandler(UsersMemory,AuthsMemory).handleRequest(req,res));
        Spark.post("/session",(req,res)->
                new LoginHandler(UsersMemory,AuthsMemory).handleLoginRequest(req,res));


        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
